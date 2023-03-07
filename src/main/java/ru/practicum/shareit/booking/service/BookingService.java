package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Service
public interface BookingService {
    BookingDto getBookingById(Long bookingId, Long userId);

    List<BookingDto> getAllBookingsOfUser(Long userId, String state);

    List<BookingDto> getAllItemsBookingsOfOwner(Long userId, String state);

    BookingDto addBooking(Long userId, BookingDtoRequest bookingDtoRequest);

    BookingDto approveBooking(Long bookingId, Long userId, Boolean approved);

    Booking getBooking(Long bookingId);
}
