package ru.practicum.shareit.exception.users;

public class UserRepositoryException extends RuntimeException {
    public UserRepositoryException(String message) {
        super(message);
    }
}