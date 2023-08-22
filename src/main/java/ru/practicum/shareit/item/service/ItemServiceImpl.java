package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<ItemDto> getAllItemsByUser(Long userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId, Sort.by("id"));
        return items.stream()
                .map(item -> getItemById(item.getId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Item with id: " + itemId + " not found"));

        Booking lastBooking;
        Booking nextBooking;
        if (item.getOwnerId().equals(userId)) {
            List<Booking> bookings = bookingRepository.findAllByItemIdAndStatusOrderByStartAsc(item.getId(), BookingStatus.APPROVED);
            lastBooking = getLastBooking(bookings);
            nextBooking = getNextBooking(bookings);
        } else {
            lastBooking = null;
            nextBooking = null;
        }
        List<Comment> comments = commentRepository.findAllByItemId(itemId);

        return ItemMapper.INSTANCE.toItemDtoOwner(item, lastBooking, nextBooking, comments);
    }

    @Transactional
    @Override
    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        validateUserExists(ownerId);
        Item item = ItemMapper.INSTANCE.toItem(itemDto);
        item.setOwnerId(ownerId);
        return ItemMapper.INSTANCE.toItemDto(itemRepository.save(item));
    }

    @Transactional
    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId) {
        Item item = getItemIfExists(itemId);
        if (!item.getOwnerId().equals(userId)) {
            throw new ForbiddenException("User with id: " + userId +
                    " is not the owner of the item: " + itemDto);
        }
        validateUserExists(userId);
        updateItemDetails(item, itemDto);
        return ItemMapper.INSTANCE.toItemDto(itemRepository.saveAndFlush(item));
    }

    @Override
    public List<ItemDto> searchAvailableItems(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return ItemMapper.INSTANCE.convertItemListToItemDTOList(itemRepository.search(text));
    }

    @Transactional
    @Override
    public CommentDto addComment(CommentDto commentDto, Long itemId, Long userId) {
        User author = getUserIfExists(userId);
        Item item = getItemIfExists(itemId);
        validateRenterCanLeaveComment(userId, itemId);
        Comment comment = CommentMapper.INSTANCE.toComment(commentDto, item, author);
        comment.setCreated(LocalDateTime.now());
        return CommentMapper.INSTANCE.toCommentDto(commentRepository.save(comment));
    }

    private Booking getNextBooking(List<Booking> bookings) {
        return bookings.stream()
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .findFirst()
                .orElse(null);
    }

    private Booking getLastBooking(List<Booking> bookings) {
        return bookings.stream()
                .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()) || booking.getStart().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(Booking::getEnd))
                .orElse(null);
    }

    private void updateItemDetails(Item item, ItemDto itemDto) {
        if (itemDto.getName() != null) item.setName(itemDto.getName());
        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());
    }

    private Item getItemIfExists(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Item with id: " + itemId + " not found"));
    }

    private User getUserIfExists(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User with id: %d not found", userId)));
    }

    private void validateUserExists(Long userId) {
        getUserIfExists(userId);
    }

    private void validateRenterCanLeaveComment(Long userId, Long itemId) {
        bookingRepository.findFirstByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(
                userId,
                itemId,
                LocalDateTime.now()).orElseThrow(
                () -> new ValidationException("Only the renter of the item can leave comments")
        );
    }

}