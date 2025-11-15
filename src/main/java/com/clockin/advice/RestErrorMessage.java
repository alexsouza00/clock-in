package com.clockin.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RestErrorMessage {
    private HttpStatus status;
    private String message;

    public RestErrorMessage(HttpStatus httpStatus, String message) {
        this.status =  httpStatus;
        this.message = message;
    }
}
