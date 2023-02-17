package ru.practicum.shareit.exception.alreadyExists;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}