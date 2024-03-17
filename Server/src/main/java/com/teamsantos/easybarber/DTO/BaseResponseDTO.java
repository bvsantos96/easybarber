package com.teamsantos.easybarber.DTO;

import lombok.Data;

@Data
public class BaseResponseDTO {
    private String responseMessage;

    public BaseResponseDTO() {
    }

    public BaseResponseDTO(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
