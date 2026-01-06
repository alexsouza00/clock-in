package com.clockin.advice;

import com.clockin.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    private ResponseEntity<RestErrorMessage> employeeNotFoundHandler(EmployeeNotFoundException exception){
            RestErrorMessage exceptionResponse = new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(DataBaseException.class)
    private ResponseEntity<RestErrorMessage> dataBaseExceptionHandler(DataBaseException exception){
        RestErrorMessage exceptionResponse = new RestErrorMessage(HttpStatus.CONFLICT, exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponse);
    }

    @ExceptionHandler(WorkdayFullException.class)
    private ResponseEntity<RestErrorMessage> workdayFullExceptionHandler(WorkdayFullException exception){
        RestErrorMessage exceptionResponse = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }

    @ExceptionHandler(InvalidDataException.class)
    private ResponseEntity<RestErrorMessage> invalidDataExceptionHandler(InvalidDataException invalidDataException){
        RestErrorMessage exceptionResponse = new RestErrorMessage(HttpStatus.BAD_REQUEST, invalidDataException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(WorkdayException.class)
    private ResponseEntity<RestErrorMessage> workdayException(WorkdayException workdayException){
        RestErrorMessage exceptionResponse = new RestErrorMessage(HttpStatus.NOT_FOUND, workdayException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }
}
