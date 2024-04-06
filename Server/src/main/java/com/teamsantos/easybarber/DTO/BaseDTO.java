package com.teamsantos.easybarber.DTO;

import java.lang.reflect.Field;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseDTO {
    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error converting object to string.";
        }
    }

    public void loadFromJSON(String json) throws Exception {
            ObjectMapper mapper = new ObjectMapper();
            Class<?> currentClass = this.getClass();
            Object obj = mapper.readValue(json, currentClass);

            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(obj);
                field.set(this, value);
            }
    }
}
