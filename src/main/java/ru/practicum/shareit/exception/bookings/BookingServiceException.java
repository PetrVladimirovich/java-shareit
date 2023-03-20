package ru.practicum.shareit.exception.bookings;

public class BookingServiceException extends RuntimeException {
    public BookingServiceException(String message) {
        super(message);
    }
}