package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private User testUser;
    private Item testItem;
    private Booking testBooking;
    private Comment testComment;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "Test User", "test@example.com");
        testItem = new Item(1L, "Test Item", "Test Description", true, 1L, null);
        testBooking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1), testItem, testUser, BookingStatus.APPROVED);
        testComment = new Comment(1L, "Test Comment", testItem, testUser, LocalDateTime.now());
    }

    @Test
    void testGetItemById() {
        Long itemId = 1L;
        Long userId = 1L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(testItem));
        when(bookingRepository.findAllByItemIdAndStatusOrderByStartAsc(itemId, BookingStatus.APPROVED)).thenReturn(Collections.singletonList(testBooking));
        when(commentRepository.findAllByItemId(itemId)).thenReturn(Collections.singletonList(testComment));

        ItemDto itemDto = itemService.getItemById(itemId, userId);

        assertNotNull(itemDto);
        assertEquals(testItem.getName(), itemDto.getName());
    }

    @Test
    void testAddItem() {
        Long ownerId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("New Item");
        itemDto.setDescription("New Description");
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(testUser));
        when(itemRepository.save(any(Item.class))).thenReturn(testItem);

        ItemDto addedItem = itemService.addItem(itemDto, ownerId);

        assertNotNull(addedItem);
        assertEquals(testItem.getName(), addedItem.getName());
    }

    @Test
    void testUpdateItem() {
        Long itemId = 1L;
        Long userId = 1L;
        ItemDto updatedItemDto = new ItemDto();
        updatedItemDto.setName("Updated Item");
        updatedItemDto.setDescription("Updated Description");
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(testItem));
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(itemRepository.saveAndFlush(any(Item.class))).thenReturn(testItem);

        ItemDto updatedItem = itemService.updateItem(itemId, updatedItemDto, userId);

        assertNotNull(updatedItem);
        assertEquals(updatedItemDto.getName(), updatedItem.getName());
    }

    @Test
    void testUpdateItemForbidden() {
        Long itemId = 1L;
        Long userId = 2L;
        ItemDto itemDto = new ItemDto();

        Item item = new Item();
        item.setOwnerId(1L);

        when(itemRepository.findById(itemId)).thenReturn(java.util.Optional.of(item));
        lenient().when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(new User()));

        assertThrows(ForbiddenException.class, () -> itemService.updateItem(itemId, itemDto, userId));
    }

    @Test
    void testSearchAvailableItems() {
        String text = "Test";
        int from = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(from, size);
        when(itemRepository.search(text, pageRequest)).thenReturn(Collections.singletonList(testItem));

        List<ItemDto> items = itemService.searchAvailableItems(text, from, size);

        assertEquals(1, items.size());
        assertEquals(testItem.getId(), items.get(0).getId());
    }

    @Test
    void testAddComment() {
        Long itemId = 1L;
        Long userId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test Comment");
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(testItem));

        when(bookingRepository.findFirstByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(
                eq(userId), eq(itemId), any(LocalDateTime.class)))
                .thenReturn(Optional.of(testBooking));

        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        CommentDto addedComment = itemService.addComment(commentDto, itemId, userId);

        assertNotNull(addedComment);
        assertEquals(commentDto.getText(), addedComment.getText());
    }

}