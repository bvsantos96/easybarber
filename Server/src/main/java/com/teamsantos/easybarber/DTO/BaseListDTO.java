package com.teamsantos.easybarber.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseListDTO<T> extends BaseResponseDTO {
    private List<T> items;

    public BaseListDTO() {
    }

    public BaseListDTO(List<T> items) {
        this.items = items;
    }

    public BaseListDTO(String message) {
        super(message);
    }
}
