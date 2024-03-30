package com.teamsantos.easybarber.DTO;

import org.springframework.data.domain.Page;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BasePageDTO<T> extends BaseResponseDTO {
    Page<T> items;

    public BasePageDTO() {
    }

    public BasePageDTO(Page<T> items) {
        this.items = items;
    }

    public BasePageDTO(String message) {
        super(message);
    }
}
