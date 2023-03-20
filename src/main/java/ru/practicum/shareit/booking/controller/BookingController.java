package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.Constants.ID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> crateBooking(@RequestHeader(ID) @NotNull Long bookerId,
                                                @RequestBody @NotNull @Valid BookingDto dto) {
        log.info("BookingController: POST /bookings");
        return new ResponseEntity<>(bookingService.create(dto, bookerId), HttpStatus.OK);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Booking> changeBookingStatus(@RequestHeader(ID) @NotNull Long ownerId,
                                                       @PathVariable("bookingId") Long bookingId,
                                                       @RequestParam("approved") @NotNull Boolean status) {
        log.info("BookingController: PATCH /bookings/{}", bookingId);
        return new ResponseEntity<>(bookingService.updateStatus(ownerId, bookingId, status), HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBookingStatus(@RequestHeader(ID) @NotNull Long userId,
                                                    @PathVariable("bookingId") Long bookingId) {
        log.info("BookingController: GET /bookings/{}", bookingId);
        return new ResponseEntity<>(bookingService.getStatus(userId, bookingId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getBookerBookings(@RequestHeader(ID) @NotNull Long userId,
                                                           @RequestParam(value = "state", defaultValue = "ALL") BookingStatusDto status,
                                                           @RequestParam(name = "from", required = false) @PositiveOrZero Integer from,
                                                           @RequestParam(name = "size", required = false) @Positive Integer size) {
        log.info("BookingController: GET /bookings");
        return new ResponseEntity<>(bookingService.getBookerBookings(userId, status, from, size), HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<Booking>> getUserBookings(@RequestHeader(ID) @NotNull Long userId,
                                                         @RequestParam(value = "state", defaultValue = "ALL") BookingStatusDto status,
                                                         @RequestParam(name = "from", required = false) @PositiveOrZero Integer from,
                                                         @RequestParam(name = "size", required = false) @Positive Integer size) {
        log.info("BookingController: GET /bookings/owner");
        return new ResponseEntity<>(bookingService.getUserBookings(userId, status, from, size), HttpStatus.OK);
    }
}