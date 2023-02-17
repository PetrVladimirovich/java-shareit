package ru.practicum.shareit.exception.alreadyExists;

public class UserAlreadyExistsException extends AlreadyExistsException {
    public UserAlreadyExistsException(Long id) {
        super("User with ID = " + id + " already exists");
    }
}
