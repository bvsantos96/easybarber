package com.teamsantos.easybarber.DTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.teamsantos.easybarber.utils.Pair;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponseDTO extends BaseDTO {
    private List<Long> ids;
    private String responseMessage;

    public BaseResponseDTO() {
    }

    public BaseResponseDTO(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public BaseResponseDTO(List<Long> ids) {
        super((ids != null && ids.size() > 0) ? ids.get(0) : null);
        this.ids = ids;
    }

    public BaseResponseDTO(Set<Long> ids, String responseMessage) {
        this(ids.stream().collect(Collectors.toList()), responseMessage);
    }

    public BaseResponseDTO(Set<Long> ids) {
        this(ids.stream().collect(Collectors.toList()), "");
    }

    public BaseResponseDTO(List<Long> ids, String responseMessage) {
        this(ids);
        this.responseMessage = responseMessage;
    }

    public BaseResponseDTO(Long id, String responseMessage) {
        this(id);
        this.responseMessage = responseMessage;
    }

    public BaseResponseDTO(Long id) {
        super(id);
        this.ids = List.of(id);
    }

    public BaseResponseDTO(Pair<Long, String> pair) {
        this(pair.getFirst(), pair.getSecond());
    }
}
