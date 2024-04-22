package com.teamsantos.easybarber.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponseDTO extends BaseDTO {
    private String responseMessage;

    public BaseResponseDTO() {
    }

    public BaseResponseDTO(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public BaseResponseDTO(Long id) {
        super(id);
    }
}
