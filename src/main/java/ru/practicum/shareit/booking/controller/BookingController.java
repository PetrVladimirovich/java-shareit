package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static ru.practicum.shareit.Constants.ID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId, @NotEmpty @RequestHeader(ID) Long userId) {
        log.info("BookingController : GET /bookings/{}", bookingId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping()
    public List<BookingDto> getAllBookingsOfUser(@NotEmpty @RequestHeader(ID) Long userId,
                                                 @RequestParam(defaultValue = "ALL", required = false) String state) {
        log.info("BookingController : GET /bookings");
        return bookingService.getAllBookingsOfUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllItemsBookingsOfOwner(@NotEmpty @RequestHeader(ID) Long userId,
                                                       @RequestParam(defaultValue = "ALL", required = false) String state) {
        log.info("BookingController : GET /bookings/owner");
        return bookingService.getAllItemsBookingsOfOwner(userId, state);
    }

    @PostMapping
    public BookingDto addBooking(@NotEmpty @RequestHeader(ID) Long userId,
                                 @Valid @RequestBody BookingDtoRequest bookingDtoRequest) {
        log.info("BookingController : POST /bookings");
        return bookingService.addBooking(userId, bookingDtoRequest);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@PathVariable Long bookingId,
                                     @NotEmpty @RequestHeader(ID) Long userId,
                                     @NotEmpty @RequestParam Boolean approved) {
        log.info("BookingController : PATCH /bookings/{}", bookingId);
        return bookingService.approveBooking(bookingId, userId, approved);
    }
}
