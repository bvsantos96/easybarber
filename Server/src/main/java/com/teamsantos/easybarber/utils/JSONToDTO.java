package com.teamsantos.easybarber.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONToDTO {

    public static String getString(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (String) field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    public static Long getLong(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (Long) field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    public static <T> List<T> fromPageDTO(JSONObject jsonObject, Class<T> clazz) {
        try {
            ArrayList<T> list = new ArrayList<T>();
            jsonObject = jsonObject.getJSONObject("items");
            JSONArray arr = jsonObject.getJSONArray("content");
            for (int i = 0; i < arr.length(); i++) {
                list.add(toDTO(arr.getJSONObject(i), clazz));
            }
            return list;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException
                | NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T toDTO(JSONObject jsonObject, Class<T> clazz) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        Constructor<T> constructor = clazz.getConstructor();
        T instance = constructor.newInstance();

        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName();
            field.setAccessible(true);
            if (jsonObject.has(fieldName)) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                if (fieldType == String.class) {
                    field.set(instance, jsonObject.getString(fieldName));
                } else if (fieldType == long.class || fieldType == Long.class) {
                    field.set(instance, jsonObject.getLong(fieldName));
                } else if (fieldType == double.class) {
                    field.setDouble(instance, jsonObject.getDouble(fieldName));
                } else if (fieldType == int.class) {
                    field.setInt(instance, jsonObject.getInt(fieldName));
                } else if (fieldType == boolean.class) {
                    field.setBoolean(instance, jsonObject.getBoolean(fieldName));
                } else if (fieldType == List.class) {
                    field.set(instance, jsonObject.getJSONArray(fieldName).toList());
                } else {
                    field.set(instance, toDTO(jsonObject.getJSONObject(fieldName), fieldType));
                }
            }
        }

        return instance;
    }
}
