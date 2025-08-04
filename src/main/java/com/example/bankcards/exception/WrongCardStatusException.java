package com.example.bankcards.exception;

public class WrongCardStatusException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Статус может быть только ACTIVE, BLOCKED или EXPIRED";

    public WrongCardStatusException() {
        super(DEFAULT_MESSAGE);
    }
}
