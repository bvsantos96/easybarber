package com.teamsantos.easybarber.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.teamsantos.easybarber.DTO.BaseDTO;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;

public class JSONToDTO {

    public static String getString(Object obj, String fieldName) {
        try {
            Field field = JSONToDTO.findFieldInHierarchy(obj.getClass(), fieldName);
            if (field == null) {
                throw new NoSuchFieldException();
            }
            field.setAccessible(true);
            return (String) field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    public static Long getLong(Object obj, String fieldName) {
        try {
            Field field = JSONToDTO.findFieldInHierarchy(obj.getClass(), fieldName);
            if (field == null) {
                throw new NoSuchFieldException();
            }
            field.setAccessible(true);
            return (Long) field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    public static Set<Long> getSetLong(Object obj, String fieldName) {
        try {
            Field field = JSONToDTO.findFieldInHierarchy(obj.getClass(), fieldName);
            if (field == null) {
                throw new NoSuchFieldException();
            }
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

    public static <K, V> Map<K, V> fromMapDTO(JSONObject jsonObject, Type clazzKey, Type clazzValue) {
        try {
            Map<K, V> map = new HashMap<>();
            Iterator<String> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String _key = keys.next();
                K key = (K) parseByType(_key, (Class<K>) clazzKey);
                V value = null;
                if (clazzValue instanceof ParameterizedType) {
                    value = _toDTO(jsonObject.get(_key), (Class<V>) ((ParameterizedType) clazzValue).getRawType(),
                            ((Class<?>) ((ParameterizedType) clazzValue).getActualTypeArguments()[0]));
                } else {
                    value = (V) parseByType(jsonObject.get(_key), (Class<V>) clazzValue);
                }
                map.put(key, value);
            }

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static <T> T _toDTO(Object jsonValue, Class<T> fieldType, Class<?> typeOfArray) {
        try {
            Object value = null;
            if (fieldType == List.class) {
                value = jsonValue.equals(JSONObject.NULL) ? null
                        : fromListDTO((JSONArray) jsonValue, typeOfArray);
            } else if (fieldType == Set.class) {
                value = jsonValue.equals(JSONObject.NULL) ? null
                        : new HashSet<>(
                                fromListDTO((JSONArray) jsonValue, typeOfArray));
            } else if (fieldType == Map.class) {
                Type genericSuperclass = fieldType.getGenericSuperclass();
                if (genericSuperclass instanceof ParameterizedType) {
                    ParameterizedType mapType = (ParameterizedType) genericSuperclass;
                    Type[] typeArguments = mapType.getActualTypeArguments();
                    value = jsonValue.equals(JSONObject.NULL) ? null
                            : fromMapDTO((JSONObject) jsonValue, typeArguments[0], typeArguments[1]);
                } else {
                    throw new Exception("Map type not found");
                }
            } else if (BaseDTO.class.isAssignableFrom(fieldType)) {
                value = jsonValue.equals(JSONObject.NULL) ? null
                        : toDTO((JSONObject) jsonValue, fieldType);
            } else {
                value = jsonValue.equals(JSONObject.NULL) ? null
                        : parseByType(jsonValue, fieldType);
            }
            return (T) value;
        } catch (Exception e) {
            return Utils.getModelMapper().map(jsonValue, fieldType);
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
                    Class<?> fieldType = findFieldTypeInHierarchy(clazz, key);
                    if (field != null) {
                        Object jsonValue = jsonObject.get(key);
                        Object value = null;
                        if (field.getType() == List.class) {
                            value = jsonValue.equals(JSONObject.NULL) ? null
                                    : fromListDTO((JSONArray) jsonValue, fieldType);
                        } else if (field.getType() == Set.class) {
                            value = jsonValue.equals(JSONObject.NULL) ? null
                                    : new HashSet<>(
                                            fromListDTO((JSONArray) jsonValue, fieldType));
                        } else if (field.getType() == Map.class) {
                            ParameterizedType mapType = (ParameterizedType) field.getGenericType();
                            Type[] typeArguments = mapType.getActualTypeArguments();
                            value = jsonValue.equals(JSONObject.NULL) ? null
                                    : fromMapDTO((JSONObject) jsonValue, typeArguments[0], typeArguments[1]);
                        } else if (BaseDTO.class.isAssignableFrom(field.getType())) {
                            value = jsonValue.equals(JSONObject.NULL) ? null
                                    : toDTO((JSONObject) jsonValue, fieldType);
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
            return Utils.getModelMapper().map(jsonObject, clazz);
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
            return Utils.getModelMapper().map(value, fieldType);
        }
    }

    public static Field findFieldInHierarchy(Class<?> clazz, String fieldName) {
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
