package com.teamsantos.easybarber.tests;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.image.ImageDTO;
import com.teamsantos.easybarber.utils.CreateTest;
import com.teamsantos.easybarber.utils.JSONToDTO;
import com.teamsantos.easybarber.utils.Utils;

public class ImageUtils {
    private final MockMvc mockMvc;
    private final String pathPrefix;

    public ImageUtils(MockMvc mockMvc, String pathPrefix) {
        this.mockMvc = mockMvc;
        this.pathPrefix = pathPrefix;
    }

    private void addImages(String jwt, List<ImageDTO> images) {
        try {
            List<Long> ids = addImages(jwt, Utils.fromListToString(images));
            for (int i = 0; ids.size() > i; i++) {
                images.get(i).setId(ids.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    private List<Long> addImages(String jwt, String items) throws Exception {
        ResultActions result = CreateTest.createOrFoundWithResult(mockMvc, String.format("%s/images", pathPrefix), jwt,
                items);
        BaseResponseDTO response = JSONToDTO.toDTO(
                new JSONObject(result.andReturn().getResponse().getContentAsString()),
                BaseResponseDTO.class);
        return response.getIds();
    }

    public void saveImages(List<ImageDTO> images, String jwt) {
        try {
            addImages(jwt, images);
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    public List<ImageDTO> getImages(String jwt) {
        try {
            ResultActions result = CreateTest.get(mockMvc, String.format("%s/images?sort=id", pathPrefix), jwt);
            result.andExpect(MockMvcResultMatchers.status().isOk());
            return JSONToDTO.fromPageDTO(
                    new JSONObject(result.andReturn().getResponse().getContentAsString()), ImageDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
        return null;
    }

    public void addImageAndCheckIfSavedOneByOne(List<ImageDTO> images, String jwt) throws Exception {
        addImageAndCheckIfSavedOneByOne(images, jwt, true);
    }

    public void addImageAndCheckIfSavedOneByOne(List<ImageDTO> images, String jwt, boolean withMain) throws Exception {
        for (ImageDTO image : images) {
            if (withMain) {
                for (ImageDTO _image : images) {
                    _image.setMain(false);
                }
                image.setMain(true);
            }
            saveImages(Collections.singletonList(image), jwt);
            ResultActions result = CreateTest
                    .get(mockMvc, String.format("%s/images/main", pathPrefix), jwt)
                    .andExpect(MockMvcResultMatchers.status().isOk());
            ImageDTO _image = JSONToDTO.toDTO(
                    new JSONObject(result.andReturn().getResponse().getContentAsString()), ImageDTO.class);
            assert _image != image;
        }
        List<ImageDTO> _images = getImages(jwt);
        assert _images.equals(images);
        setMain(images.get(0), jwt);
    }

    public void addImageAndCheckIfSaved(List<ImageDTO> images, String jwt) throws Exception {
        if (images == null || images.isEmpty()) {
            return;
        }
        deleteImages(images.stream().map(ImageDTO::getId).toList(), jwt);
        saveImages(images, jwt);
        List<ImageDTO> _images = getImages(jwt);
        assert _images.equals(images);
    }

    public void deleteImages(List<Long> images, String jwt) throws Exception {
        CreateTest.deleteOk(mockMvc, String.format("%s/images", pathPrefix), jwt, Utils.fromListToString(images));
    }

    public void deleteImagesCheckAndReset(List<ImageDTO> images, String jwt) throws Exception {
        if (images == null || images.isEmpty()) {
            return;
        }
        List<ImageDTO> imagesNotDeleted = images.stream().filter(e -> (e.getId() % 2 == 0)).toList();
        if (!imagesNotDeleted.isEmpty()) {
            imagesNotDeleted.get(0).setMain(true);
        }
        deleteImages(images.stream().filter(e -> (e.getId() % 2 != 0)).map(ImageDTO::getId).toList(), jwt);
        List<ImageDTO> _images = getImages(jwt);
        for (int i = 0; i < _images.size(); i++) {
            assert _images.get(i).equals(imagesNotDeleted.get(i));
        }
        // Reset
        CreateTest.delete(mockMvc, String.format("%s/images", pathPrefix), jwt,
                Utils.fromListToString(imagesNotDeleted.stream().map(ImageDTO::getId).toList()));
        for (ImageDTO image : images) {
            image.setId(null);
            image.setMain(false);
        }
        if (images.size() > 0) {
            images.get(0).setMain(true);
        }
        addImages(jwt, images);
    }

    public void deleteMain(List<ImageDTO> images, String jwt) throws Exception {
        if (images == null || images.isEmpty()) {
            return;
        }
        CreateTest.deleteOk(mockMvc, String.format("%s/images", pathPrefix), jwt,
                Utils.fromListToString(List.of(images.get(0).getId())));
        if (images.size() > 0) {
            ResultActions result = CreateTest.get(mockMvc, String.format("%s/images/main", pathPrefix), jwt);
            result.andExpect(MockMvcResultMatchers.status().isOk());
            ImageDTO imageDTO = JSONToDTO.toDTO(
                    new JSONObject(result.andReturn().getResponse().getContentAsString()), ImageDTO.class);
            images.get(1).setMain(true);
            assert imageDTO.equals(images.get(1));
            CreateTest.delete(mockMvc, String.format("%s/images", pathPrefix), jwt,
                    Utils.fromListToString(images.subList(1, images.size())));
            images.get(1).setMain(false);
        }
        images.get(0).setMain(true);
        addImages(jwt, images);

    }

    public void setMain(ImageDTO image, String jwt) {
        if (image == null) {
            return;
        }
        try {
            CreateTest.putWJWT(mockMvc, String.format("%s/images/main/%d", pathPrefix, image.getId()), jwt);
            image.setMain(true);
            ResultActions result = CreateTest.get(mockMvc, String.format("%s/images/main", pathPrefix), jwt);
            result.andExpect(MockMvcResultMatchers.status().isOk());
            ImageDTO imageDTO = JSONToDTO.toDTO(
                    new JSONObject(result.andReturn().getResponse().getContentAsString()), ImageDTO.class);
            assert imageDTO.equals(image);
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }
}
