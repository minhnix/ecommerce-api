package com.nix.ecommerceapi.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message ) {
        super(message);
    }
}
