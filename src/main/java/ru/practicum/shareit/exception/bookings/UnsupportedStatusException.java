package ru.practicum.shareit.exception.bookings;

import ru.practicum.shareit.exception.based.UnavailableException;

public class UnsupportedStatusException extends UnavailableException {
    public UnsupportedStatusException(String status) {
        super(String.format("Unknown state: %s", status));
    }
}
