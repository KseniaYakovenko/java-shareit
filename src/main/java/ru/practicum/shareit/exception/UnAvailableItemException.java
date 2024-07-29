package ru.practicum.shareit.exception;

public class UnAvailableItemException extends RuntimeException {

    public UnAvailableItemException(String message) {
        super(message);
    }
}