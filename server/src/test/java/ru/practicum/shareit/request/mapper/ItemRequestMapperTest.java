package ru.practicum.shareit.request.mapper;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ItemRequestMapperTest {

    private final ItemRequestMapperImpl itemRequestMapper = new ItemRequestMapperImpl();

    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        Long userId = 1L;
        Long requestId = 1L;

        User user = new User(
                userId,
                "userName1",
                "1@gmail.com");

        Item item1 = new Item();
        item1.setId(1L);
        Item item2 = new Item();
        item2.setId(2L);

        itemRequest = new ItemRequest(
                requestId,
                "description",
                user,
                Instant.now(),
                Sets.set(item1, item2));
    }

    @Test
    void toItemRequestDto() {
        ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequest);

        assertEquals(itemRequest.getId(), itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(), itemRequestDto.getDescription());
    }

    @Test
    void requestToRequestAnswerDtoShouldReturnNull() {
        ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(null);

        assertNull(itemRequestDto);
    }

    @Test
    void toItemRequestShouldReturnNull() {
        ItemRequest request1 = itemRequestMapper.toItemRequest(null, null);
        assertNull(request1);
    }

    @Test
    void convertItemRequestListToItemRequestDTOList() {
        List<ItemRequest> itemRequestList = List.of(itemRequest, itemRequest);

        List<ItemRequestDto> dtos = itemRequestMapper.convertItemRequestListToItemRequestDTOList(itemRequestList);

        assertEquals(itemRequestList.size(), dtos.size());
    }

}