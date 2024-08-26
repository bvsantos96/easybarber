package com.teamsantos.easybarber.DTO;

import java.util.List;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseListDTO<T> extends BaseResponseDTO {
    private List<T> items;

    public BaseListDTO() {
    }

    public BaseListDTO(Set<T> items) {
        this.items = List.copyOf(items);
    }

    public BaseListDTO(List<T> items) {
        this.items = items;
    }

    public BaseListDTO(String message) {
        super(message);
    }
}
