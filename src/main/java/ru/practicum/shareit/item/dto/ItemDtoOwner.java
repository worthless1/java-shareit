package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemDtoOwner extends ItemDto {
    private BookingShortDto lastBooking;

    private BookingShortDto nextBooking;

    private List<CommentDto> comments;
}
