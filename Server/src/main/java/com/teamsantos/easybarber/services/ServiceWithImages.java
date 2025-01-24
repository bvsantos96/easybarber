package com.teamsantos.easybarber.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.teamsantos.easybarber.DTO.image.ImageDTO;
import com.teamsantos.easybarber.entities.base.EntityWithImages;
import com.teamsantos.easybarber.entities.base.Image;
import com.teamsantos.easybarber.exceptions.GenericNotFoundException;
import com.teamsantos.easybarber.repositories.base.ImageRepository;
import com.teamsantos.easybarber.utils.Utils;

import jakarta.persistence.EntityManager;
import net.coobird.thumbnailator.Thumbnails;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class ServiceWithImages<T extends EntityWithImages<T, E>, E extends Image<T, E>> {
    protected final JpaRepository<T, Long> repository;
    protected final ImageRepository<T, E> imageRepository;
    protected final ModelMapper modelMapper;
    protected final EntityManager entityManager;
    protected Class<T> entityClass;
    protected Class<E> imageClass;

    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @SuppressWarnings("unchecked")
    public ServiceWithImages(JpaRepository<T, Long> repository, ImageRepository<T, E> imageRepository,
            ModelMapper modelMapper, EntityManager entityManager) {
        this.repository = repository;
        this.imageRepository = imageRepository;
        this.modelMapper = modelMapper;
        this.entityManager = entityManager;
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
        this.imageClass = (Class<E>) genericSuperclass.getActualTypeArguments()[1];
    }

    public E parseImage(EntityManager entityManager, Object image, Long entityId) {
        E imageEntity = Utils.getModelMapper().map(image, imageClass);
        imageEntity.setEntity(entityManager.getReference(entityClass, entityId));
        return imageEntity;
    }

    // TODO: limit the amount of images updloaded
    @Transactional
    public List<Long> saveImages(long entityId, Collection<ImageDTO> images) {
        List<E> imagesToAdd = new ArrayList<>();
        boolean newMain = false;

        for (ImageDTO image : images) {
            if ((image.getData() != null && !image.getData().isEmpty())) {
                if (image.getMain() != null && image.getMain()) {
                    if (newMain) {
                        image.setMain(false);
                    } else {
                        newMain = true;
                    }
                }

                // Generate a unique file name
                String fileName = UUID.randomUUID().toString() + ".jpg";

                E imageEntity = Utils.getModelMapper().map(image, imageClass);
                imageEntity.setEntity(entityManager.getReference(entityClass, entityId));
                imageEntity.setFileName(fileName);
                imageEntity.setData(addImageToBucket(image.getData(), fileName));
                imagesToAdd.add(imageEntity);
            }
        }
        if (newMain) {
            imageRepository.removeMainFlag(entityId);
        } else if (!imageRepository.existsMain(entityId)) {
            if (!imagesToAdd.isEmpty()) {
                imagesToAdd.get(0).setMain(true);
            }
        }
        if (!imagesToAdd.isEmpty()) {
            return imageRepository.saveAll(imagesToAdd).stream().map(Image::getId).toList();
        }

        return new ArrayList<>();
    }

    @Transactional
    public void deleteImages(long entityId, Set<Long> imageIds) {
        boolean mainDeleted = imageRepository.isAnyMainImage(entityId, imageIds);

        imageRepository.deleteImages(entityId, imageIds);

        if (mainDeleted) {
            Long id = imageRepository.findOldestImageId(entityId);
            if (id != null) {
                imageRepository.setNewMain(entityId, id);
            }
        }
    }

    @Transactional
    public void setMain(long entityId, long imageId) throws GenericNotFoundException {
        imageRepository.removeMainFlag(entityId);
        E i = imageRepository.findByIdAndEntityId(imageId, entityId)
                .orElseThrow(() -> new GenericNotFoundException("Image not found"));
        i.setMain(true);
        imageRepository.save(i);
    }

    @Transactional(readOnly = true)
    public Page<ImageDTO> getImages(Long entityId, Pageable pageable) throws NotFoundException {
        return imageRepository.findByEntityId(entityId, pageable);
    }

    @Transactional(readOnly = true)
    public ImageDTO getMainImage(Long entityId) throws GenericNotFoundException {
        return imageRepository.findMainImage(entityId)
                .orElseThrow(() -> new GenericNotFoundException("Image not found"));
    }


    /////////////////////////////////////////////////////////////////////////////////////////
    // 🚀 Private Methods Section🛠️
    /////////////////////////////////////////////////////////////////////////////////////////

    private String addImageToBucket(String base64Data, String fileName) throws Exception{
        S3Client s3Client = null;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Data);

            // Compress and resize the image
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(new ByteArrayInputStream(decodedBytes))
                    .size(1080, 1080)
                    .outputQuality(1)
                    .outputFormat("jpg")
                    .toOutputStream(outputStream);

            byte[] compressedBytes = outputStream.toByteArray();

            // Upload to S3
            s3Client = S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKeyId, secretKey)))
                    .build();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType("image/jpeg")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(compressedBytes));

            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image to S3 bucket", e);
        } finally {
            if (s3Client != null) {
                s3Client.close();
            }
        }
    }

    private void deleteImageFromBucket(String fileName) {
        S3Client s3Client = null;
        try {
            s3Client = S3Client.builder()
                    .region(Region.of(region)) 
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKeyId, secretKey)))
                    .build();

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete image from S3 bucket", e);
        } finally {
            if (s3Client != null) {
                s3Client.close();
            }
        }
    }
}
