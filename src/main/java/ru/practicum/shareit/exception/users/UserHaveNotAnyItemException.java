package ru.practicum.shareit.exception.users;

import ru.practicum.shareit.exception.based.NotFoundException;

public class UserHaveNotAnyItemException extends NotFoundException {
    public UserHaveNotAnyItemException() {
        super("User isn't owner any item");
    }
}
