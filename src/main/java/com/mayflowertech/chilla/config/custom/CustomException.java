package com.mayflowertech.chilla.config.custom;

public class CustomException extends Throwable {

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
