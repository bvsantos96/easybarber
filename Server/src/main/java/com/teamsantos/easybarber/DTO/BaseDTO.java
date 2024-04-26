package com.teamsantos.easybarber.DTO;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

@Getter
@Setter
public class BaseDTO {
    @Nullable
    private Long id;

    public BaseDTO(@Nullable Long id) {
        if (id != null && id.equals(0L))
            this.id = null;
        else
            this.id = id;
    }

    public BaseDTO() {
    }

    public BaseDTO addId(Long id) {
        this.id = id;
        return this;
    }

    public Long getId() {
        if (id == null) {
            return 0L;
        }
        return id;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error converting object to string.";
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        BaseDTO other = (BaseDTO) obj;
        return getId().equals(other.getId());
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
