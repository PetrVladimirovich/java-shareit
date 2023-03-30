package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.ItemStatus;

import java.time.LocalDateTime;
import static ru.practicum.shareit.utils.Consts.TIME_PATTERN;

@AllArgsConstructor
@Data
public class BookingItemDto {
    private Long id;
    private Long bookerId;
    @JsonFormat(pattern = TIME_PATTERN)
    private LocalDateTime start;
    @JsonFormat(pattern = TIME_PATTERN)
    private LocalDateTime end;
    private ItemStatus status;
}