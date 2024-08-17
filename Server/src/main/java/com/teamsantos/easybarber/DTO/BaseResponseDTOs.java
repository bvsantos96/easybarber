package com.teamsantos.easybarber.DTO;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponseDTOs extends BaseResponseDTO {
    private List<Long> ids;

    public BaseResponseDTOs() {
    }

    public BaseResponseDTOs(String responseMessage) {
        super(responseMessage);
    }

    public BaseResponseDTOs(List<Long> ids, String responseMessage) {
        super(responseMessage);
        this.ids = ids;
    }
}
