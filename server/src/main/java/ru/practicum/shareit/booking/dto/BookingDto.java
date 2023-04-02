package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

import static ru.practicum.shareit.utils.Consts.TIME_PATTERN;


@AllArgsConstructor
@Data
public class BookingDto {
    private Long itemId;
    @JsonFormat(pattern = TIME_PATTERN)
    private LocalDateTime start;
    @JsonFormat(pattern = TIME_PATTERN)
    private LocalDateTime end;
}