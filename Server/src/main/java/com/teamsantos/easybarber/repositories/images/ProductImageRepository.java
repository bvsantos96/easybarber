package com.teamsantos.easybarber.repositories.images;

import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.Product;
import com.teamsantos.easybarber.entities.images.ProductImage;
import com.teamsantos.easybarber.repositories.base.ImageRepository;

@Repository
public interface ProductImageRepository extends ImageRepository<Product, ProductImage> {
}
