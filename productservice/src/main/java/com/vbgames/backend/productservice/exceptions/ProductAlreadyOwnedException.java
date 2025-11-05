package com.vbgames.backend.productservice.exceptions;

public class ProductAlreadyOwnedException extends RuntimeException {

    public ProductAlreadyOwnedException() {
        super();
    }
    public ProductAlreadyOwnedException(String message) {
        super(message);
    }

}
