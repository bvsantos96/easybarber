package com.teamsantos.easybarber.exceptions;

public class InvalidTokenException extends Exception {
    public InvalidTokenException() {
        super("Invalid token");
    }
}
