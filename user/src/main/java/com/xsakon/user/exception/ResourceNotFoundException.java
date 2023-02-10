package com.xsakon.user.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

    private static final Logger log = LoggerFactory.getLogger(ResourceNotFoundException.class);

    public ResourceNotFoundException(String message){
        super(message);
        log.error("Resource not found exception with message: {}", message);
    }
}