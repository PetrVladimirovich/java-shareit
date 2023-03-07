package ru.practicum.shareit.exception.users;

public class RepeatEmailException extends RuntimeException {
    public RepeatEmailException(String email) {
        super(String.format("User with email - %s already exists", email));
    }
}
