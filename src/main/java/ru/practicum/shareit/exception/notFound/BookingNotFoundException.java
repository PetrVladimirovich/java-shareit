package ru.practicum.shareit.exception.notFound;

public class BookingNotFoundException extends NotFoundException {
    public BookingNotFoundException(Long id) {
        super("Booking with ID = " + id + " does not exist");
    }
}
