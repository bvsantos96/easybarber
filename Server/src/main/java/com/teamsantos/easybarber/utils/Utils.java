package com.teamsantos.easybarber.utils;

public class Utils {
    public static void setFieldIfNotNullOrEmpty(String field1, String field2) {
        if (field2 == null) {
            field1 = field2;
        }
    }

    public static void setFieldIfNotNullOrEmpty(int field1, int field2) {
        if (field2 == 0) {
            field1 = field2;
        }
    }

    public static void setFieldIfNotNullOrEmpty(long field1, long field2) {
        if (field2 == 0) {
            field1 = field2;
        }
    }

    public static void setFieldIfNotNullOrEmpty(float field1, float field2) {
        if (field2 == 0) {
            field1 = field2;
        }
    }

    public static void setFieldIfNotNullOrEmpty(double field1, double field2) {
        if (field2 == 0) {
            field1 = field2;
        }
    }

    public static void setFieldIfNotNullOrEmpty(Object field1, Object field2) {
        if (field2 == null) {
            field1 = field2;
        }
    }
}
