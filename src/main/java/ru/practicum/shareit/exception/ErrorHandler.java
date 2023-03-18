package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.exception.bookings.BookingServiceException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.exception.items.CommentServiceException;
import ru.practicum.shareit.exception.items.ItemRepositoryException;
import ru.practicum.shareit.exception.items.ItemServiceException;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.exception.requests.ItemRequestServiceException;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.exception.users.UserRepositoryException;
import ru.practicum.shareit.exception.users.UserServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Map;


@Slf4j
@ControllerAdvice(assignableTypes = {ItemController.class, UserController.class, BookingController.class,
        ItemRequestController.class})
class ErrorHandler {

    @ExceptionHandler(UserRepositoryException.class)
    public ResponseEntity<String> userRepositoryHandler(final RuntimeException e) {
        log.debug(e.getMessage());
        if (StringUtils.containsIgnoreCase(e.getMessage(), "the box has already been used")) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<String> userServiceHandler(final RuntimeException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemServiceException.class)
    public ResponseEntity<String> itemServiceHandler(final RuntimeException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemRepositoryException.class)
    public ResponseEntity<String> itemRepositoryHandler(final RuntimeException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> sqlHandler(final RuntimeException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BookingServiceException.class)
    public ResponseEntity<String> bookingServiceHandler(final RuntimeException e) {
        log.debug(e.getMessage());
        if (StringUtils.containsIgnoreCase(e.getMessage(), "data is not available") ||
                StringUtils.containsIgnoreCase(e.getMessage(), "there is no access to change the status") ||
                StringUtils.containsIgnoreCase(e.getMessage(), "booking your own things is prohibited"
                )) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> methodArgumentHandler(final MethodArgumentTypeMismatchException e) {
        log.debug(e.getMessage());
        if (StringUtils.containsIgnoreCase(e.getValue().toString(), "UNSUPPORTED_STATUS")) {
            return new ResponseEntity<>(Map.of("error", "Unknown state: " + e.getValue().toString()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommentServiceException.class)
    public ResponseEntity<String> commentServiceHandler(final RuntimeException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemRequestServiceException.class)
    public ResponseEntity<String> itemRequestServiceHandler(final RuntimeException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> constraintViolationHandler(final RuntimeException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}