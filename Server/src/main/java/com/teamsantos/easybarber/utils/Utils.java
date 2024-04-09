package com.teamsantos.easybarber.utils;

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
}
