package ru.practicum.shareit.exception;

public class IncorrectActionException extends RuntimeException {

    public IncorrectActionException(String message) {
        super(message);
    }
}