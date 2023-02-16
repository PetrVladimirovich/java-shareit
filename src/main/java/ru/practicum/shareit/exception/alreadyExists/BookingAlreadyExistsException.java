package ru.practicum.shareit.exception.alreadyExists;

public class BookingAlreadyExistsException extends AlreadyExistsException{
    public BookingAlreadyExistsException(Long id) {
        super("Booking with ID = " + id + " already exists");
    }
}
