package ru.practicum.shareit.exception.notFound;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Long id) {
        super("User with ID = " + id + " does not exist");
    }
}
