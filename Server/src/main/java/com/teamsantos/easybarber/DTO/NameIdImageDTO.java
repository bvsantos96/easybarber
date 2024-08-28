package com.teamsantos.easybarber.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NameIdImageDTO extends BaseDTO {
    private String name;
    private String image;

    public NameIdImageDTO(Long id, String name) {
        super(id);
        this.name = name;
    }

    public NameIdImageDTO(Long id, String name, String image) {
        this(id, name);
        this.image = image;
    }
}
