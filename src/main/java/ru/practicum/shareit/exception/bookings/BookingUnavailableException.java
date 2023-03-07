package ru.practicum.shareit.exception.bookings;

import ru.practicum.shareit.exception.based.NotFoundException;

public class BookingUnavailableException extends NotFoundException {
    public BookingUnavailableException(String message) {
        super(message);
    }
}
