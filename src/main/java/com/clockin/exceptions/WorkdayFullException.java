package com.clockin.exceptions;

public class WorkdayFullException extends RuntimeException {

    public WorkdayFullException(){
        super("Workday is completed");
    }
    public WorkdayFullException(String message) {
        super(message);
    }
}
