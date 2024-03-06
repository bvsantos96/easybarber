package com.teamsantos.easybarber.Exceptions;

public class UserAlreadyExists extends Exception {

    public UserAlreadyExists() {
        super("Mobile number already registered.");
    }

    public UserAlreadyExists(String message) {
        super(message);
    }
}
