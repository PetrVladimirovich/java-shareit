package ru.practicum.shareit.exception.items;

import ru.practicum.shareit.exception.based.NotFoundException;

public class ItemNotFoundException extends NotFoundException {
    public ItemNotFoundException(Long itemId) {
        super(String.format("Item with id - %d not found", itemId));
    }
}

