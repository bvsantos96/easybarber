package com.teamsantos.easybarber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamsantos.easybarber.entities.Product;
import com.teamsantos.easybarber.entities.images.ProductImage;
import com.teamsantos.easybarber.services.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController extends ImageController<Product, ProductImage> {

    @Autowired
    public ProductController(ProductService service) {
        super(service);
    }

    @Override
    public boolean canEdit(long entityId) {
        return ((ProductService) service).canEdit(entityId);
    }
}
