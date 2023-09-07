package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingAnswerDto;
import ru.practicum.shareit.booking.dto.BookingOwnerDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingMapperTest {

    private final BookingMapperImpl bookingMapper = new BookingMapperImpl();

    private User user;
    private Item item;
    private Booking booking;

    @BeforeEach
    void setUp() {
        user = new User(
                1L,
                "name",
                "email@gmail.com");

        item = new Item(
                1L,
                "name",
                "description",
                true,
                1L,
                new ItemRequest());

        booking = new Booking(
                1L,
                LocalDateTime.now().plusHours(5),
                LocalDateTime.now().plusDays(10),
                item,
                user,
                BookingStatus.WAITING);
    }

    @Test
    void toBookingOutDto() {
        BookingAnswerDto bookingAnswerDto = bookingMapper.toBookingAnswerDto(booking);

        assertEquals(booking.getId(), bookingAnswerDto.getId());
        assertEquals(booking.getStart(), bookingAnswerDto.getStart());
        assertEquals(booking.getEnd(), bookingAnswerDto.getEnd());
        assertEquals(booking.getItem().getId(), bookingAnswerDto.getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingAnswerDto.getBooker().getId());
        assertEquals(booking.getStatus(), bookingAnswerDto.getStatus());
    }

    @Test
    void toBookingShortDto() {
        BookingOwnerDto bookingDtoOwner = bookingMapper.toBookingOwnerDto(booking);

        assertEquals(booking.getId(), bookingDtoOwner.getId());
        assertEquals(booking.getBooker().getId(), bookingDtoOwner.getBookerId());
    }

    @Test
    void toBooking() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto(1L,
                LocalDateTime.now().plusHours(5),
                LocalDateTime.now().plusDays(10),
                1L,
                new UserDto(1L,
                        "name",
                        "email@gmail.com"),
                BookingStatus.WAITING);

        Booking booking = bookingMapper.toBooking(bookingRequestDto, user, item);

        assertEquals(bookingRequestDto.getStart(), booking.getStart());
        assertEquals(bookingRequestDto.getEnd(), booking.getEnd());
        assertEquals(bookingRequestDto.getItemId(), booking.getItem().getId());
        assertEquals(bookingRequestDto.getBooker().getId(), booking.getBooker().getId());
        assertEquals(bookingRequestDto.getStatus(), booking.getStatus());

    }

    @Test
    void convertBookingListToBookingAnswerDTOList() {
        List<BookingAnswerDto> bookings = bookingMapper.convertBookingListToBookingAnswerDTOList(List.of(booking));

        assertEquals(booking.getId(), bookings.get(0).getId());
    }

}