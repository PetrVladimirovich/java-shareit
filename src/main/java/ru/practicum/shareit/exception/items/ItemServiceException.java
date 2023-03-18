package ru.practicum.shareit.exception.items;

public class ItemServiceException extends RuntimeException {
    public ItemServiceException(String message) {
        super(message);
    }
}