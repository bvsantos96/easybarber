package com.teamsantos.easybarber.DTO;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseListDTO<T> extends BaseResponseDTO {
    private List<T> items;
}
