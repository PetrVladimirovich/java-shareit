package ru.practicum.shareit.exception.bookings;

import ru.practicum.shareit.exception.based.UnavailableException;

public class DoubleApprovingException extends UnavailableException {
    public DoubleApprovingException(Long bookingId) {
        super(String.format("Booking with id-%d is already approved", bookingId));
    }
}
