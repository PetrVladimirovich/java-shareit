package ru.practicum.shareit.exception.alreadyExists;

public class UserAlreadyExistsByEmailException extends AlreadyExistsException {
    public UserAlreadyExistsByEmailException(String email) {
        super("User with Email = " + email + " already exists");
    }
}
