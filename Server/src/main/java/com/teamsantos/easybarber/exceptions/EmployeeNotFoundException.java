package com.teamsantos.easybarber.exceptions;

public class EmployeeNotFoundException extends GenericNotFoundException {

    public EmployeeNotFoundException() {
        super("Employee");
    }

    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
