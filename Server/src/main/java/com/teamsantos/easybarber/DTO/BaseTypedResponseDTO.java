package com.teamsantos.easybarber.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseTypedResponseDTO<T> {
    private T value;
    private String responseMessage;

    public BaseTypedResponseDTO(T value) {
        this.value = value;
    }
}
