package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingAnswerDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.List;

public interface BookingService {
    List<BookingAnswerDto> getAllBookingsByUser(Long userId, String state);

    List<BookingAnswerDto> getAllBookingsAllItemsByOwner(Long userId, String state);

    BookingAnswerDto getBookingById(Long userId, Long bookingId);

    BookingAnswerDto saveBooking(BookingRequestDto bookingDto, Long userId);

    BookingAnswerDto updateBooking(Long bookingId, Boolean approved, Long userId);

}
