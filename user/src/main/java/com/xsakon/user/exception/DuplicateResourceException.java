package com.xsakon.user.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(DuplicateResourceException.class);

    public DuplicateResourceException(String message) {
        super(message);
        log.error("Duplicate resource exception with message: {}", message);
    }

}