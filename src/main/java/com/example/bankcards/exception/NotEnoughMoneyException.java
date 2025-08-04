package com.example.bankcards.exception;

public class NotEnoughMoneyException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "На карте недостаточно средств для перевода";
    public NotEnoughMoneyException() {
        super(DEFAULT_MESSAGE);
    }
}
