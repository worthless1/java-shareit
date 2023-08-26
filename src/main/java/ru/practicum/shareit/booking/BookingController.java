package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<BookingAnswerDto>> getAllBookingsByUser(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        List<BookingAnswerDto> bookingAnswerDtos = bookingService.getAllBookingsByUser(userId, state);

        return ResponseEntity.ok().body(bookingAnswerDtos);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingAnswerDto>> getAllBookingsAllItemsByOwner(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        List<BookingAnswerDto> bookingAnswerDtos = bookingService.getAllBookingsAllItemsByOwner(userId, state);

        return ResponseEntity.ok().body(bookingAnswerDtos);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingAnswerDto> getBookingById(@PathVariable Long bookingId,
                                                           @RequestHeader(USER_ID_HEADER) Long userId) {
        BookingAnswerDto bookingAnswerDto = bookingService.getBookingById(userId, bookingId);

        return ResponseEntity.ok(bookingAnswerDto);
    }

    @PostMapping
    @Validated
    public ResponseEntity<BookingAnswerDto> saveBooking(@Valid @RequestBody BookingRequestDto bookingInDto,
                                                        @RequestHeader(USER_ID_HEADER) Long userId) {
        BookingAnswerDto bookingAnswerDto = bookingService.saveBooking(bookingInDto, userId);

        return ResponseEntity.ok(bookingAnswerDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingAnswerDto> updateItem(@PathVariable Long bookingId, @RequestParam Boolean approved,
                                                       @RequestHeader(USER_ID_HEADER) Long userId) {
        BookingAnswerDto bookingAnswerDto = bookingService.updateBooking(bookingId, approved, userId);

        return ResponseEntity.ok(bookingAnswerDto);
    }

}