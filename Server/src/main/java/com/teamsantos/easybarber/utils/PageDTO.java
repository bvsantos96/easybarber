package com.teamsantos.easybarber.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class PageDTO {
    public static <T, U> Page<U> toDTO(ModelMapper modelMapper, Page<T> pages, Class<U> classType, Pageable pageable) {
        List<U> dtos = pages.stream()
                .map(item -> modelMapper.map(item, classType))
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, pages.getTotalElements());
    }
}
