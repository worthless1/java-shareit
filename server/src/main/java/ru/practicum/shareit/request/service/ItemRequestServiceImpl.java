package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.utils.PaginationMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemRequestDto> getAllItemRequestsByUser(Long userId) {
        checkUserExistence(userId);

        return ItemRequestMapper.INSTANCE.convertItemRequestListToItemRequestDTOList(
                itemRequestRepository.findAllByRequestorId(userId));
    }

    @Override
    public List<ItemRequestDto> getAllItemRequestsByOtherUsers(Long userId, Integer from, Integer size) {
        checkUserExistence(userId);
        PageRequest page = PaginationMapper.toPage(from, size);

        return ItemRequestMapper.INSTANCE.convertItemRequestListToItemRequestDTOList(
                itemRequestRepository.findAllByRequestorIdNot(userId, page));
    }

    @Override
    public ItemRequestDto getItemRequestById(Long requestId, Long userId) {
        checkUserExistence(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Request with id = " + requestId + " not found"));

        return ItemRequestMapper.INSTANCE.toItemRequestDto(itemRequest);
    }

    @Transactional
    @Override
    public ItemRequestDto saveItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id = " + userId + " not found"));

        ItemRequest itemRequest = ItemRequestMapper.INSTANCE.toItemRequest(itemRequestDto, user);
        return ItemRequestMapper.INSTANCE.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    private void checkUserExistence(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id = " + userId + " not found"));
    }

}
