package com.andersen.marketplace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CategoryBadRequestException extends ResponseStatusException {

    public CategoryBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, (message));
    }
}
