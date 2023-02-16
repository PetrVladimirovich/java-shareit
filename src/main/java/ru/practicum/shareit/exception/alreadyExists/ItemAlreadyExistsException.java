package ru.practicum.shareit.exception.alreadyExists;

public class ItemAlreadyExistsException extends AlreadyExistsException {
    public ItemAlreadyExistsException(Long id) {
        super("Item with ID = " + id + " already exists");
    }
}
