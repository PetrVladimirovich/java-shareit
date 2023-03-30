package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.utils.Consts.TIME_PATTERN;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemRequestDto {
    private Long id;
    private String description;
    private long requestor;
    @JsonFormat(pattern = TIME_PATTERN)
    private LocalDateTime created;
    private List<ItemForItemRequestDto> items;
}