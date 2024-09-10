package com.teamsantos.easybarber.exceptions;

public class GenericNotFoundException extends Exception {

    public GenericNotFoundException(String resource) {
        super(String.format("%s not found", resource));
    }

    public GenericNotFoundException(String message, boolean isPlural) {
        super(message);
    }

    public GenericNotFoundException setMessage(String message) {
        return new GenericNotFoundException(message, false);
    }
}
