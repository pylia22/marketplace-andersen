package com.andersen.marketplace.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class ProductNotFoundException extends ResponseStatusException {

    public ProductNotFoundException(UUID id) {
        super(HttpStatus.NOT_FOUND, String.format("Product with id: %s not found", id));
    }
}
