package com.teamsantos.easybarber.exceptions;

public class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException() {
        super("Mobile number already registered.");
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
