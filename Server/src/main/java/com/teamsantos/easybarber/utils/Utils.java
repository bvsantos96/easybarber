package com.teamsantos.easybarber.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.modelmapper.ModelMapper;

import com.teamsantos.easybarber.DTO.EstablishmentDTO;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
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

    private static ModelMapper _modelMapper;

    public static ModelMapper getModelMapper() {
        if (_modelMapper == null) {
            _modelMapper = createModelMapper();
        }
        return _modelMapper;
    }

    public static ModelMapper createModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        // Type mapping for Establishment to EstablishmentDTO
        modelMapper.typeMap(Establishment.class, EstablishmentDTO.class)
                .addMappings(mapper -> mapper.map(src -> src.getLocation(), EstablishmentDTO::setLocation));
        _modelMapper = modelMapper;
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

    public static String fromListToString(List<?> items) {
        if (items == null) {
            return "[]";
        }
        JSONArray jsonArray = new JSONArray();
        for (Object item : items) {
            jsonArray.put(item);
        }
        return jsonArray.toString();
    }

    public static LocalTime getEndOfDayTime() {
        return LocalTime.parse("23:59:59");
    }

    public static String getTimeNow(String format) {
        if (format != null && !format.isEmpty()) {
            return java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern(format));
        }
        return java.time.LocalTime.now().toString();
    }

    public static DAY_OF_WEEK getDayOfWeek(LocalDate date) {
        return DAY_OF_WEEK.valueOf(DayOfWeek.from(date).name());
    }

    public static Set<DAY_OF_WEEK> getDaysOfWeek(LocalDate startDate, LocalDate endDate) {
        Set<DAY_OF_WEEK> daysOfWeek = new HashSet<>();
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return daysOfWeek;
        }
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            daysOfWeek.add(DAY_OF_WEEK.valueOf(DayOfWeek.from(date).name()));
        }
        return daysOfWeek;
    }

    public static DAY_OF_WEEK getTodayDayOfWeek() {
        return DAY_OF_WEEK.valueOf(DayOfWeek.from(LocalDate.now()).name());
    }

    public static boolean beforeOrEqual(LocalDate date1, LocalDate date2) {
        return date1.isBefore(date2) || date1.isEqual(date2);
    }

    public static boolean afterOrEqual(LocalDate date1, LocalDate date2) {
        return date1.isAfter(date2) || date1.isEqual(date2);
    }

    public static boolean beforeOrEqual(LocalTime date1, LocalTime date2) {
        return date1.isBefore(date2) || date1.equals(date2);
    }

    public static boolean afterOrEqual(LocalTime date1, LocalTime date2) {
        return date1.isAfter(date2) || date1.equals(date2);
    }

    public static String formatStringToLIKE(String value) {
        if (value != null) {
            return String.format("%%%s%%", value.trim());
        }
        return value;
    }
}
