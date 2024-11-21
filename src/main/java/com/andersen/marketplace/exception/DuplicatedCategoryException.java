package com.andersen.marketplace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DuplicatedCategoryException extends ResponseStatusException {

    public DuplicatedCategoryException(String name) {
        super(HttpStatus.CONFLICT, String.format("Category with name: %s already exists", name));
    }
}
