package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.validator.BookingDtoValid;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

import static ru.practicum.shareit.Constants.TIME_DATE_PATTERN;

@AllArgsConstructor
@BookingDtoValid
@Data
public class BookingDto {
    private Long itemId;

    @JsonFormat(pattern = TIME_DATE_PATTERN)
    @FutureOrPresent
    private LocalDateTime start;

    @JsonFormat(pattern = TIME_DATE_PATTERN)
    @FutureOrPresent
    private LocalDateTime end;
}