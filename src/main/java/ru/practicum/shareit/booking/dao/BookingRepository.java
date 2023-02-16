package ru.practicum.shareit.booking.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository {

    List<Booking> getAllBookings();

    Optional<Booking> getBookingById(Long bookingId);

    Booking createBooking(Booking booking);

    Booking updateBooking(Booking booking);

    void deleteBookingById(Long bookingId);

    boolean bookingExists(Long bookingId);

}