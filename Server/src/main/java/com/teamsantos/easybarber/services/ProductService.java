package com.teamsantos.easybarber.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamsantos.easybarber.DTO.filters.ProductFilter;
import com.teamsantos.easybarber.DTO.filters.ProductRequestFilter;
import com.teamsantos.easybarber.DTO.product.ProductDTO;
import com.teamsantos.easybarber.DTO.product.ProductRequestsDTO;
import com.teamsantos.easybarber.entities.Product;
import com.teamsantos.easybarber.entities.ProductType;
import com.teamsantos.easybarber.entities.images.ProductImage;
import com.teamsantos.easybarber.exceptions.GenericNotFoundException;
import com.teamsantos.easybarber.repositories.ProductRepository;
import com.teamsantos.easybarber.repositories.images.ProductImageRepository;

import jakarta.persistence.EntityManager;

@Service
public class ProductService extends ServiceWithImages<Product, ProductImage> {
    private final ProductRepository productRepository;
    private final UserService userService;
    private final AppointmentService appointmentService;

    @Autowired
    public ProductService(ProductRepository repository,
            ProductImageRepository imageRepository,
            ModelMapper modelMapper,
            EntityManager entityManager,
            UserService userService,
            AppointmentService appointmentService) {
        super(repository, imageRepository, modelMapper, entityManager);
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.productRepository = repository;
    }

    @Transactional(readOnly = false)
    public Long create(ProductDTO productDTO) throws Exception {
        Product product = modelMapper.map(productDTO, Product.class);
        for (Long ids : productDTO.getProductTypeIds()) {
            product.addProductType(entityManager.getReference(ProductType.class, ids));
        }
        product = repository.save(product);
        this.saveImages(product.getId(), productDTO.getImages());
        return product.getId();
    }

    @Transactional(readOnly = false)
    public void disableProduct(Set<Long> ids) {
        for (Long id : ids) {
            Optional<Product> product = repository.findById(id);
            if (product.isPresent()) {
                product.get().setAvailable(false);
                repository.save(product.get());
            }
        }
    }

    @Transactional(readOnly = false)
    public void addSuggestionToClient(Set<Long> productIds, Long userId) {
        userService.addSuggestionToUser(userId, productIds);
    }

    @Transactional(readOnly = false)
    public void requestProduct(Set<Long> productIds, Long appointmentId) throws GenericNotFoundException {
        for (Long id : productIds) {
            Optional<Product> product = repository.findById(id);
            if (!product.isPresent()) {
                throw new GenericNotFoundException("Product");
            }
        }
        appointmentService.createProductRequest(appointmentId, productIds);
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getProductSuggestions(long userId) {
        return userService.getProductSuggestions(userId);
    }

    @Transactional(readOnly = true)
    public List<ProductRequestsDTO> getProductRequests(ProductRequestFilter filter) {
        return appointmentService.getProductRequests(filter);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> getProducts(ProductFilter filter, Pageable pageable) {
        return productRepository.getProducts(filter, pageable);
    }

	public Long update(ProductDTO product) {
        Product entity = repository.findById(product.getId()).get();
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        entity.setAvailable(true);
        entity.setProductTypes(Set.of());
        for (Long ids : product.getProductTypeIds()) {
            entity.addProductType(entityManager.getReference(ProductType.class, ids));
        }
        repository.save(entity);
        return entity.getId();
	}
}
