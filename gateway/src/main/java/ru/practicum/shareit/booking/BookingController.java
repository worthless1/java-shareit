package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getAllBookingsByUser(@RequestHeader(USER_ID_HEADER) Long userId,
                                                       @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                       @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                       @Positive @RequestParam(defaultValue = "10") Integer size) {
        return bookingClient.getAllBookingsByUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsAllItemsByOwner(@RequestHeader(USER_ID_HEADER) Long userId,
                                                                @RequestParam(defaultValue = "ALL") String state,
                                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        return bookingClient.getAllBookingsAllItemsByOwner(userId, state, from, size);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@PathVariable Long bookingId,
                                                 @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingClient.getBookingById(bookingId, userId);
    }

    @PostMapping
    @Validated
    public ResponseEntity<Object> saveBooking(@Valid @RequestBody BookingRequestDto bookingInDto,
                                              @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingClient.saveBooking(userId, bookingInDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateItem(@PathVariable Long bookingId, @RequestParam Boolean approved,
                                             @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingClient.updateBooking(bookingId, userId, approved);
    }

}