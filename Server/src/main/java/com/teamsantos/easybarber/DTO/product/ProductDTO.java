package com.teamsantos.easybarber.DTO.product;

import java.util.Set;
import java.util.stream.Collectors;

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
    private long establishmentId;
    private Set<Long> productTypeIds;
    private String name;
    private String description;
    private Double price;
    private Set<ImageDTO> images;

    public ProductDTO(Long id, long establishmentId, Object productTypeIds, String name, String description,
            Double price,
            ProductImage image) {
        this.id = id;
        this.establishmentId = establishmentId;
        if (productTypeIds != null) {
            if (productTypeIds instanceof String) {
                this.productTypeIds = Set.of(((String) productTypeIds).split(",")).stream().map(Long::parseLong)
                        .collect(Collectors.toSet());
            } else if (productTypeIds instanceof Long) {
                this.productTypeIds = Set.of((Long) productTypeIds);
            }
        } else {
            this.productTypeIds = Set.of();
        }
        this.name = name;
        this.description = description;
        this.price = price;
        this.images = image != null ? Set.of(new ImageDTO(image.getId(), image.getData(), true)) : Set.of();
    }
}
