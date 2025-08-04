package com.example.bankcards.exception;

public class CardNotBelongsToUserException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Операция невозможна. Карта не принадлежит этому пользователю";
    public CardNotBelongsToUserException() {
        super(DEFAULT_MESSAGE);
    }
}
