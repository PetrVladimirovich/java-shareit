package ru.practicum.shareit.exception.items;

import ru.practicum.shareit.exception.based.UnavailableException;

public class ItemUnavailableException extends UnavailableException {
    public ItemUnavailableException(Long itemId) {
        super(String.format("Item with id - %d unavailable", itemId));
    }
}
