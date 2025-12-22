package ru.otus.hw.exceptions;

public class UserAnswerReadException extends RuntimeException {
    public UserAnswerReadException(String message, Throwable ex) {
        super(message, ex);
    }

    public UserAnswerReadException(String message) {
        super(message);
    }
}
