package ru.practicum.shareit.exception.based;


public class UnavailableException extends RuntimeException {
    public UnavailableException(String message) {
        super(message);
    }
}

