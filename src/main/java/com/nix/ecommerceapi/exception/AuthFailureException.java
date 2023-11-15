package com.nix.ecommerceapi.exception;

public class AuthFailureException extends RuntimeException {
    public AuthFailureException(String message) {
        super(message);
    }
}
