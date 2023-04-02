package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingStatusDto;

import java.util.List;
import static ru.practicum.shareit.utils.Consts.REQUESTOR_ID_TAG;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> crateBooking(@RequestHeader(REQUESTOR_ID_TAG) Long bookerId,
                                                @RequestBody BookingDto dto) {
        log.info("BookingController : POST /bookings with ID: {}", bookerId);
        return new ResponseEntity<>(bookingService.create(dto, bookerId), HttpStatus.OK);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Booking> changeBookingStatus(@RequestHeader(REQUESTOR_ID_TAG) Long ownerId,
                                                       @PathVariable("bookingId") Long bookingId,
                                                       @RequestParam("approved") Boolean status) {
        log.info("BookingController : PATCH /bookings/{} with IDowner: {}", bookingId, ownerId);
        return new ResponseEntity<>(bookingService.updateStatus(ownerId, bookingId, status), HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBookingStatus(@RequestHeader(REQUESTOR_ID_TAG) Long userId,
                                                    @PathVariable("bookingId") Long bookingId) {
        log.info("BookingController : GET /bookings/{} with IDuser: {}", bookingId, userId);
        return new ResponseEntity<>(bookingService.getBooking(userId, bookingId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getBookerBookings(@RequestHeader(REQUESTOR_ID_TAG) Long userId,
                                                           @RequestParam(value = "state", defaultValue = "ALL") BookingStatusDto status,
                                                           @RequestParam(name = "from", required = false) Integer from,
                                                           @RequestParam(name = "size", required = false) Integer size) {
        log.info("BookingController : GET /bookings with IDuser: {}", userId);
        return new ResponseEntity<>(bookingService.getBookerBookings(userId, status, from, size).getContent(), HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<Booking>> getUserBookings(@RequestHeader(REQUESTOR_ID_TAG) Long userId,
                                                         @RequestParam(value = "state", defaultValue = "ALL") BookingStatusDto status,
                                                         @RequestParam(name = "from", required = false) Integer from,
                                                         @RequestParam(name = "size", required = false) Integer size) {
        log.info("BookingController : GET /bookings/owner with IDuser: {}", userId);
        return new ResponseEntity<>(bookingService.getUserBookings(userId, status, from, size).getContent(), HttpStatus.OK);
    }
}