package com.example.bankcards.exception;

public class CardNotFoundException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Карта с таким id не найдена в базе данных";

    public CardNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
