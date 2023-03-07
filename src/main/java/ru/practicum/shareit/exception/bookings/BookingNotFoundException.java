package ru.practicum.shareit.exception.bookings;

import ru.practicum.shareit.exception.based.NotFoundException;

public class BookingNotFoundException extends NotFoundException {
    public BookingNotFoundException(Long bookingId) {
        super(String.format("Booking with id - %d not found", bookingId));
    }
}
