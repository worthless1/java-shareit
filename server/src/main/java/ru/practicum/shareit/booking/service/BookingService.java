package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingAnswerDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    List<BookingAnswerDto> getAllBookingsByUser(Long userId, State state, Integer from, Integer size);

    List<BookingAnswerDto> getAllBookingsAllItemsByOwner(Long userId, State state, Integer from, Integer size);

    BookingAnswerDto getBookingById(Long userId, Long bookingId);

    BookingAnswerDto saveBooking(BookingRequestDto bookingDto, Long userId);

    BookingAnswerDto updateBooking(Long bookingId, Boolean approved, Long userId);

}
