package ru.practicum.shareit.exception.users;

import ru.practicum.shareit.exception.based.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Long userId) {
        super(String.format("User with id - %d not found", userId));
    }
}
