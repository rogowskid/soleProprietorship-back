package com.example.soleproprietorship.common;

public class ResponseEntityException extends RuntimeException{
    public ResponseEntityException(String message) {
        super(message);
    }
}
