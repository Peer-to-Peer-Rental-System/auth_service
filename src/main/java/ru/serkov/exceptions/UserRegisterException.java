package ru.serkov.exceptions;

public class UserRegisterException extends RuntimeException {
    public UserRegisterException(String msg) {
        super(msg);
    }

    public UserRegisterException(String msg, Exception ex) {
        super(msg, ex);
    }
}
