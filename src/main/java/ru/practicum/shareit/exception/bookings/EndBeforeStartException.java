package ru.practicum.shareit.exception.bookings;

import ru.practicum.shareit.exception.based.UnavailableException;

public class EndBeforeStartException extends UnavailableException {
    public EndBeforeStartException() {
        super("Start date of booking after end date");
    }
}
