package com.andersen.marketplace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CategoryNotFoundException extends ResponseStatusException {

    public CategoryNotFoundException(String categoryIdentifier) {
        super(HttpStatus.NOT_FOUND, String.format("Product with identifier: %s not found", categoryIdentifier));
    }
}
