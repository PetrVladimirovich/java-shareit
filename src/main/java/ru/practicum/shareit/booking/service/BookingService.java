package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Service
public interface BookingService {

    List<Booking> getAllBookings();

    Booking getBookingById(Long bookingId);

    Booking createBooking(Booking booking);

    Booking updateBooking(Booking booking);

    void deleteBookingById(Long bookingId);

}