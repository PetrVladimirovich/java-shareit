package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingStatusDto;
import ru.practicum.shareit.booking.exception.BookingServiceException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.ItemStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingServiceImpl(@Qualifier("dbStorage") UserRepository userRepository, BookingRepository bookingRepository,
                              BookingMapper bookingMapper, @Qualifier("itemDbStorage") ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.bookingMapper = bookingMapper;
        this.itemRepository = itemRepository;
    }

    @Override
    public Booking create(BookingDto dto, Long bookerId) {
        Booking booking = bookingMapper.toNewBooking(dto);
        booking.setBooker(userRepository.getById(bookerId));
        booking.setItem(itemRepository.getById(dto.getItemId()));
        if (booking.getItem().getOwner().equals(bookerId)) {
            throw new BookingServiceException("booking your own things is prohibited");
        }
        if (booking.getItem().getAvailable().equals(true)) {
            bookingRepository.save(booking);
            log.info("BookingServiceImpl.create() {}", booking.toString());
            return booking;
        }
        throw new BookingServiceException("booking is not available");
    }

    @Override
    public Booking updateStatus(Long ownerId, Long bookingId, Boolean status) {
        Booking booking = bookingRepository.findByIdAndItemOwner(bookingId, ownerId)
                .orElseThrow(() -> new BookingServiceException("there is no access to change the status"));
        if (booking.getStatus() == ItemStatus.APPROVED) {
            throw new BookingServiceException("status approved, modification prohibited");
        }
        if (status.equals(true)) {
            booking.setStatus(ItemStatus.APPROVED);
        } else {
            booking.setStatus(ItemStatus.REJECTED);
        }
        bookingRepository.save(booking);
        log.info("BookingServiceImpl.updateStatus() {}", booking.toString());
        return booking;
    }

    @Override
    public Booking getBooking(Long userId, Long bookingId) {
        return bookingRepository
                .findByIdAndBookerIdAndItemOwner(bookingId, userId)
                .orElseGet(() -> {
                    throw new BookingServiceException("data is not available");
                });
    }

    @Override
    public Page<Booking> getBookerBookings(Long userId, BookingStatusDto status, Integer from, Integer size) {
        userRepository.getById(userId);
        Pageable page;
        if (size == null || from == null) {
            page = Pageable.unpaged();
        } else {
            Sort sortByCreated = Sort.by(Sort.Direction.DESC, "start");
            page = PageRequest.of(from / size, size, sortByCreated);
        }
        Page<Booking> bookingPage = bookingRepository.findByBookerId(userId, page);
        List<Booking> bookings = getBookings(status, bookingPage.getContent());
        return new PageImpl<>(bookings);
    }

    @Override
    public Page<Booking> getUserBookings(Long userId, BookingStatusDto status, Integer from, Integer size) {
        userRepository.getById(userId);

        Pageable page;
        if (size == null || from == null) {
            page = Pageable.unpaged();
        } else {
            Sort sortByCreated = Sort.by(Sort.Direction.DESC, "start");
            page = PageRequest.of(from / size, size, sortByCreated);
        }
        Page<Booking> bookingPage = bookingRepository.findByItemOwner(userId, page);
        List<Booking> bookings = getBookings(status, bookingPage.getContent());
        return new PageImpl<>(bookings);
    }

    private static List<Booking> getBookings(BookingStatusDto status, List<Booking> bookings) {
        List<Booking> bookingsForReturn = new ArrayList<>();
        for (Booking booking : bookings) {
            switch (status) {
                case CURRENT:
                    if (booking.getStart().isBefore(LocalDateTime.now()) && booking.getEnd().isAfter(LocalDateTime.now())) {
                        bookingsForReturn.add(booking);
                    }
                    break;
                case PAST:
                    if (booking.getEnd().isBefore(LocalDateTime.now())) {
                        bookingsForReturn.add(booking);
                    }
                    break;
                case FUTURE:
                    if (booking.getStart().isAfter(LocalDateTime.now())) {
                        bookingsForReturn.add(booking);
                    }
                    break;
                case WAITING:
                    if (booking.getStatus() == ItemStatus.WAITING) {
                        bookingsForReturn.add(booking);
                    }
                    break;
                case REJECTED:
                    if (booking.getStatus() == ItemStatus.REJECTED) {
                        bookingsForReturn.add(booking);
                    }
                    break;
                default:
                    bookingsForReturn.add(booking);
            }
        }
        return bookingsForReturn.stream()
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .collect(Collectors.toList());
    }
}