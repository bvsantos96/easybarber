package com.teamsantos.easybarber.utils;

import org.modelmapper.ModelMapper;

import com.teamsantos.easybarber.DTO.EstablishmentDTO;
import com.teamsantos.easybarber.entities.Establishment;

public class Utils {
    public static String setFieldIfNotNullOrEmpty(String field1, String field2) {
        if (field2 != null && !field2.isEmpty()) {
            return field2;
        }
        return field1;
    }

    public static Integer setFieldIfNotNullOrEmpty(Integer field1, Integer field2) {
        if (field2 != null && field2 != 0) {
            return field2;
        }
        return field1;
    }

    public static Long setFieldIfNotNullOrEmpty(Long field1, Long field2) {
        if (field2 != null && field2 != 0L) {
            return field2;
        }
        return field1;
    }

    public static Float setFieldIfNotNullOrEmpty(Float field1, Float field2) {
        if (field2 != null && field2 != 0.0f) {
            return field2;
        }
        return field1;
    }

    public static Double setFieldIfNotNullOrEmpty(Double field1, Double field2) {
        if (field2 != null && field2 != 0) {
            return field2;
        }
        return field1;
    }

    public static Object setFieldIfNotNullOrEmpty(Object field1, Object field2) {
        if (field2 != null) {
            return field2;
        }
        return field1;
    }

    private static ModelMapper modelMapper;

    public static ModelMapper getModelMapper() {
        if (modelMapper == null) {
            modelMapper = createModelMapper();
        }
        return modelMapper;
    }

    public static ModelMapper createModelMapper() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        modelMapper.typeMap(Establishment.class, EstablishmentDTO.class)
                .addMappings(mapper -> mapper.map(src -> src.getLocation(), EstablishmentDTO::setLocation));
        return modelMapper;
    }

    public static boolean equalsWithNull(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null) {
            return true;
        }
        if (obj1 == null || obj2 == null) {
            return false;
        }
        return obj1.equals(obj2);
    }
}
