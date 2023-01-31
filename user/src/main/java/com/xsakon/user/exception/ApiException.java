package com.xsakon.user.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ApiException {
    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timeStamp;
    private final String path;


    public ApiException(String message, HttpStatus httpStatus, ZonedDateTime timeStamp, String path) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timeStamp = timeStamp;
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ZonedDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getPath() {
        return path;
    }
}
