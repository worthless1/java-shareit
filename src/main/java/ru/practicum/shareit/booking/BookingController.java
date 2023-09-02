package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingAnswerDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @GetMapping
    public List<BookingAnswerDto> getAllBookingsByUser(@RequestHeader(USER_ID_HEADER) Long userId,
                                                       @RequestParam(defaultValue = "ALL") String state,
                                                       @RequestParam(defaultValue = "0") Integer from,
                                                       @RequestParam(defaultValue = "10") Integer size) {
        return bookingService.getAllBookingsByUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingAnswerDto> getAllBookingsAllItemsByOwner(@RequestHeader(USER_ID_HEADER) Long userId,
                                                                @RequestParam(defaultValue = "ALL") String state,
                                                                @RequestParam(defaultValue = "0") Integer from,
                                                                @RequestParam(defaultValue = "10") Integer size) {
        return bookingService.getAllBookingsAllItemsByOwner(userId, state, from, size);
    }

    @GetMapping("/{bookingId}")
    public BookingAnswerDto getBookingById(@PathVariable Long bookingId,
                                           @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @PostMapping
    @Validated
    public BookingAnswerDto saveBooking(@Valid @RequestBody BookingRequestDto bookingInDto,
                                        @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingService.saveBooking(bookingInDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingAnswerDto updateItem(@PathVariable Long bookingId, @RequestParam Boolean approved,
                                       @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingService.updateBooking(bookingId, approved, userId);
    }

}