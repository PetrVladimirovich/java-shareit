package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.alreadyExists.BookingAlreadyExistsException;
import ru.practicum.shareit.exception.notFound.BookingNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRep;

    @Override
    public List<Booking> getAllBookings() {
        log.debug("BookingService: done findAllBookings.");
        return bookingRep.getAllBookings();
    }

    @Override
    public Booking getBookingById(Long bookingId) {
        Booking booking =  bookingRep.getBookingById(bookingId).orElseThrow(
                () -> new BookingNotFoundException(bookingId)
        );
        log.debug("BookingService: done findBookingById - {}.", booking);
        return booking;
    }

    @Override
    public Booking createBooking(Booking booking) {
        if (booking.getId() != null && bookingRep.bookingExists(booking.getId())) {
            throw new BookingAlreadyExistsException(booking.getId());
        }
        booking = bookingRep.createBooking(booking);
        log.debug("BookingService: done createBooking - {}.", booking);
        return booking;
    }

    @Override
    public Booking updateBooking(Booking booking) {
        if (!bookingRep.bookingExists(booking.getId())) {
            throw new BookingNotFoundException(booking.getId());
        }
        log.debug("BookingService: done updateBooking - {}.", booking);
        return bookingRep.updateBooking(booking);
    }

    @Override
    public void deleteBookingById(Long bookingId) {
        if (!bookingRep.bookingExists(bookingId)) {
            throw new BookingNotFoundException(bookingId);
        }
        bookingRep.deleteBookingById(bookingId);
        log.debug("BookingService: done deleteBookingById - ID {}.", bookingId);
    }
}