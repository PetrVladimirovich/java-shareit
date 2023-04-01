package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

import static ru.practicum.shareit.utils.Consts.TIME_PATTERN_WITH_MILLISECONDS;

@AllArgsConstructor
@Data
public class CommentDto {
    private Long id;
    @NotBlank
    private String text;
    private String authorName;
    @JsonFormat(pattern = TIME_PATTERN_WITH_MILLISECONDS)
    private LocalDateTime created;
}