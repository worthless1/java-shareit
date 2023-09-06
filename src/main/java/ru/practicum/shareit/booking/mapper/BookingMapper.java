package ru.practicum.shareit.booking.mapper;

import lombok.Generated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingAnswerDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingOwnerDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;


@Mapper
@Generated
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    BookingAnswerDto toBookingAnswerDto(Booking booking);

    @Mapping(target = "bookerId", source = "booking.booker.id")
    BookingOwnerDto toBookingOwnerDto(Booking booking);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booker", source = "booker")
    @Mapping(target = "item", source = "item")
    Booking toBooking(BookingRequestDto bookingDto, User booker, Item item);

    List<BookingAnswerDto> convertBookingListToBookingAnswerDTOList(List<Booking> list);

}