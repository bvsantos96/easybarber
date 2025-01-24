package com.teamsantos.easybarber.DTO.product;

import java.util.Set;

import com.teamsantos.easybarber.DTO.image.ImageDTO;
import com.teamsantos.easybarber.entities.images.ProductImage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private Long employeeId;
    private long establishmentId;
    private Set<Long> productTypeIds;
    private String name;
    private String description;
    private Double price;
    private Set<ImageDTO> images;

    public ProductDTO(Long id, long establishmentId, Long employeeId, Set<Long> productTypeIds, String name,
            String description, Double price, ProductImage image) {
        this.id = id;
        this.employeeId = employeeId;
        this.establishmentId = establishmentId;
        this.productTypeIds = productTypeIds;
        this.name = name;
        this.description = description;
        this.price = price;
        this.images = Set.of(new ImageDTO(image.getId(), image.getData(), true));
    }
}
