package ru.practicum.shareit.exception.notFound;

public class ItemNotFoundException extends NotFoundException {
    public ItemNotFoundException(Long id) {
        super("Item with ID = " + id + " does not exist");
    }
}
