package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingAnswerDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    BookingAnswerDto toBookingOutDto(Booking booking);

    @Mapping(target = "bookerId", source = "booking.booker.id")
    BookingShortDto toBookingDtoOwner(Booking booking);

    Booking toBooking(BookingRequestDto bookingDto);

    List<BookingAnswerDto> convertBookingListToBookingOutDTOList(List<Booking> list);

}