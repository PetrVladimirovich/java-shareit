package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class ItemDto {
    private Long id;
    @NotBlank(message = "the 'name' field cannot be empty")
    private String name;
    @NotBlank(message = "the 'description' field cannot be empty")
    private String description;
    @NotNull(message = "the 'available' field is required")
    private Boolean available;
    private Long request;
}