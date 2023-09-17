package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.booking.dto.BookingOwnerDto;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ItemOwnerDto extends ItemDto {

    private BookingOwnerDto lastBooking;
    private BookingOwnerDto nextBooking;
    private List<CommentDto> comments;

}
