package com.teamsantos.easybarber.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NameIdImagePriceDTO extends NameIdImageDTO {
    private Double price;

    public NameIdImagePriceDTO(Long id, String name, String image, double price) {
        super(id, name, image);
        this.price = price;
    }
}
