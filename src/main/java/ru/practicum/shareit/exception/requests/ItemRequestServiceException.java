package ru.practicum.shareit.exception.requests;

public class ItemRequestServiceException extends RuntimeException {
    public ItemRequestServiceException(String message) {
        super(message);
    }
}