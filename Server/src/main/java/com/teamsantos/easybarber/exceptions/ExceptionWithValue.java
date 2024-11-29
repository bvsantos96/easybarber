package com.teamsantos.easybarber.exceptions;

public class ExceptionWithValue extends Exception {
    private final Object value;

    public ExceptionWithValue(String message, Object value) {
        super(message);
        this.value = value;
    }

    public <T> T getValue(Class<T> clazz) {
        return clazz.cast(value);
    }
}
