package com.teamsantos.easybarber.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;

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

    public static Set<Long> getSetLong(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (Set<Long>) field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    public static <T> List<T> fromPageDTO(JSONObject jsonObject, Class<T> clazz) {
        try {
            ArrayList<T> list = new ArrayList<T>();
            JSONArray arr;
            try {
                jsonObject = jsonObject.getJSONObject("items");
                arr = jsonObject.getJSONArray("content");
            } catch (Exception e) {
                arr = jsonObject.getJSONArray("items");
            }
            for (int i = 0; i < arr.length(); i++) {
                list.add(toDTO(arr.getJSONObject(i), clazz));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> fromListDTO(JSONArray jsonArray, Class<T> clazz) {
        try {
            ArrayList<T> list = new ArrayList<T>();
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    list.add(toDTO(jsonArray.getJSONObject(i), clazz));
                } catch (Exception e) {
                    list.add((T) parseByType(jsonArray.getString(i), clazz));
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T toDTO(JSONObject jsonObject, Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            T instance = constructor.newInstance();

            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                try {
                    Field field = findFieldInHierarchy(clazz, key);
                    if (field != null) {
                        Object jsonValue = jsonObject.get(key);
                        Object value = null;
                        if (field.getType() == List.class) {
                            value = jsonValue.equals(JSONObject.NULL) ? null
                                    : fromListDTO((JSONArray) jsonValue, findFieldTypeInHierarchy(clazz, key));
                        } else if (field.getType() == Set.class) {
                            value = jsonValue.equals(JSONObject.NULL) ? null
                                    : new HashSet<>(
                                            fromListDTO((JSONArray) jsonValue, findFieldTypeInHierarchy(clazz, key)));
                        } else {
                            value = jsonValue.equals(JSONObject.NULL) ? null
                                    : parseByType(jsonValue, field.getType());
                        }
                        field.setAccessible(true);
                        field.set(instance, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object parseByType(Object value, Class<?> fieldType) {
        if (fieldType == int.class || fieldType == Integer.class) {
            return Integer.parseInt(value.toString());
        } else if (fieldType == long.class || fieldType == Long.class) {
            return Long.parseLong(value.toString());
        } else if (fieldType == double.class || fieldType == Double.class) {
            return Double.parseDouble(value.toString());
        } else if (fieldType == String.class) {
            return value.toString();
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            return Boolean.parseBoolean(value.toString());
        } else if (fieldType == DAY_OF_WEEK.class) {
            return DAY_OF_WEEK.valueOf(value.toString());
        } else if (fieldType == LocalTime.class) {
            return LocalTime.parse(value.toString());
        } else if (fieldType == LocalDate.class) {
            return LocalDate.parse(value.toString());
        } else {
            return value;
        }
    }

    private static Field findFieldInHierarchy(Class<?> clazz, String fieldName) {
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            try {
                Field field = currentClass.getDeclaredField(fieldName);
                return field;
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }

    private static Class<?> findFieldTypeInHierarchy(Class<?> clazz, String fieldName) {
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            try {
                Field field = currentClass.getDeclaredField(fieldName);
                Class<?> fieldType = field.getType();

                if (List.class.isAssignableFrom(fieldType) || Set.class.isAssignableFrom(fieldType)) {
                    Type genericType = field.getGenericType();
                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType paramType = (ParameterizedType) genericType;
                        Type[] typeArguments = paramType.getActualTypeArguments();
                        if (typeArguments.length > 0) {
                            if (typeArguments[0] instanceof Class) {
                                return (Class<?>) typeArguments[0];
                            }
                        }
                    }
                    return Object.class; // Default to Object if we can't determine the specific type
                }

                return fieldType;
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }
}
