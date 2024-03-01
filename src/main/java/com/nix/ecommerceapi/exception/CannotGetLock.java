package com.nix.ecommerceapi.exception;

public class CannotGetLock extends RuntimeException{
    public CannotGetLock(String message ) {
        super(message);
    }
}
