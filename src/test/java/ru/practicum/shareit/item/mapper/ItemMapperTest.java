package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

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
    void toItem() {
        Item item = itemMapper.toItem(null);

        assertNull(item);
    }

    @Test
    void convertItemListToItemDTOList() {
        List<ItemDto> items = itemMapper.convertItemListToItemDTOList(null);

        assertNull(items);
    }

}