package ru.practicum.shareit.exception.users;

public class UserServiceException extends RuntimeException {
    public UserServiceException(String message) {
        super(message);
    }
}