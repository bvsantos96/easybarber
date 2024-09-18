package com.teamsantos.easybarber.exceptions;

public class ForbidenException extends Exception {
    public ForbidenException(String resource) {
        super(String.format("No permission to access %s", resource));
    }
}
