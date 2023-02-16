package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    @NotNull(message = "the 'email' field is required", groups = {Create.class})
    @Email(groups = {Create.class, Update.class})
    private String email;
    @NotBlank(message = "the 'name' field cannot be empty", groups = {Create.class})
    private String name;
}