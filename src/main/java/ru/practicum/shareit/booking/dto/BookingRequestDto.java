package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BookingRequestDto {
    private Long id;

    @NotNull(message = "The start date and time of the booking cannot be null")
    @FutureOrPresent(message = "The start date and time of the booking cannot be in the past")
    private LocalDateTime start;

    @NotNull(message = "The end date and time of the booking cannot be nullified")
    @FutureOrPresent(message = "The end date and time of the booking cannot be in the past")
    private LocalDateTime end;

    @NotNull(message = "Reservations have to have a thing")
    private Long itemId;

    private UserDto booker;
    private BookingStatus status;
}