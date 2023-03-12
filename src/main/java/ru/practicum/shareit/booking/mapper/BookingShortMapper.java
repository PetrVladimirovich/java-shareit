package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingShortMapper {
    @Mapping(target = "bookerId", source = "model.booker.id")
    BookingDtoResponse toDTO(Booking model);
}
