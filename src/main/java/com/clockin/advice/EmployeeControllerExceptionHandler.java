package com.clockin.advice;

import com.clockin.exceptions.DataBaseException;
import com.clockin.exceptions.EmployeeNotFoundException;
import com.clockin.exceptions.WorkdayFullException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class EmployeeControllerExceptionHandler extends ResponseEntityExceptionHandler {

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
}
