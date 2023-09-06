package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingOwnerDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;


class ItemMapperTest {

    private final ItemMapperImpl itemMapper = new ItemMapperImpl();

    @Test
    void toItemDtoOwner() {
        ItemOwnerDto itemOwnerDto = itemMapper
                .toItemOwnerDto(null, null, null, null);

        assertNull(itemOwnerDto);

    }

    @Test
    void toItemDto() {
        ItemDto itemDto = itemMapper.toItemDto(null);

        assertNull(itemDto);
    }

    @Test
    void toItem() {
        Item item = itemMapper.toItem(null);

        assertNull(item);
    }

    @Test
    void toCommentDto() {
        CommentDto commentDto = CommentMapper.INSTANCE.toCommentDto(null);

        assertThat(commentDto, nullValue());
    }

    @Test
    void toBookingOwnerDto() {
        BookingOwnerDto bookingOwnerDto = ItemMapper.toBookingOwnerDto(null);

        assertThat(bookingOwnerDto, nullValue());
    }

    @Test
    void convertItemListToItemDTOList() {
        List<ItemDto> items = itemMapper.convertItemListToItemDTOList(null);

        assertNull(items);
    }

}