package com.teamsantos.easybarber.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO extends BaseDTO {
    private String url;

    public ImageDTO(Long id, String url) {
        super(id);
        this.url = url;
    }
}
