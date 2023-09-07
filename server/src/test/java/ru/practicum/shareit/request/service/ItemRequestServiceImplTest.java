package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Test
    void testGetAllItemRequestsByUser() {
        Long userId = 1L;
        List<ItemRequest> itemRequestList = new ArrayList<>();
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findAllByRequestorId(userId)).thenReturn(itemRequestList);

        List<ItemRequestDto> result = itemRequestService.getAllItemRequestsByUser(userId);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetAllItemRequestsByOtherUsers() {
        Long userId = 1L;
        int from = 0;
        int size = 10;
        List<ItemRequest> itemRequestList = new ArrayList<>();
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findAllByRequestorIdNot(userId, PageRequest.of(from, size))).thenReturn(itemRequestList);

        List<ItemRequestDto> result = itemRequestService.getAllItemRequestsByOtherUsers(userId, from, size);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetItemRequestById() {
        Long requestId = 1L;
        Long userId = 1L;
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(requestId);
        itemRequest.setDescription("Test request");
        itemRequest.setCreated(Instant.now());
        itemRequest.setItems(new HashSet<>());
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(itemRequest));

        ItemRequestDto result = itemRequestService.getItemRequestById(requestId, userId);

        assertNotNull(result);
        assertEquals(requestId, result.getId());
        assertEquals("Test request", result.getDescription());
        assertNotNull(result.getCreated());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void testSaveItemRequest() {
        Long userId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Test request");
        itemRequestDto.setCreated(Instant.now());
        itemRequestDto.setItems(new HashSet<>());
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any())).thenAnswer(invocation -> {
            ItemRequest savedRequest = invocation.getArgument(0);
            savedRequest.setId(1L); // Simulate the saved request with an ID
            return savedRequest;
        });

        ItemRequestDto result = itemRequestService.saveItemRequest(itemRequestDto, userId);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Test request", result.getDescription());
        assertNotNull(result.getCreated());
        assertTrue(result.getItems().isEmpty());
    }

}