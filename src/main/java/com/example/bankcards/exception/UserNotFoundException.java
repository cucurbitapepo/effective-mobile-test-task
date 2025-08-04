package com.example.bankcards.exception;

public class UserNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Пользователь с таким id не найден в базе данных";

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
