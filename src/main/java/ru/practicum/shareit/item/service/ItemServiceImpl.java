package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;

    public ItemServiceImpl(ItemStorage itemStorage, UserService userService) {
        this.itemStorage = itemStorage;
        this.userService = userService;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        Item item = ItemMapper.INSTANCE.dtoToItem(itemDto);
        // check if there is a user with this id
        userService.getUserById(ownerId);

        item.setOwner(ownerId);
        item = itemStorage.addItem(item);
        return ItemMapper.INSTANCE.itemToDto(item);
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId) {

        Item updatedItem = ItemMapper.INSTANCE.dtoToItem(itemDto);
        // check if there is a user with this id
        userService.getUserById(userId);
        // Check if the user owns the item
        if (!itemStorage.findItemById(itemId).getOwner().equals(userId)) {
            throw new ForbiddenException("User with ID " + userId + " does not own the item with ID " + itemId);
        }

        updatedItem = itemStorage.updateItem(itemId, updatedItem, userId);

        return ItemMapper.INSTANCE.itemToDto(updatedItem);
    }

    @Override
    public ItemDto getItem(Long itemId) {
        Item item = itemStorage.findItemById(itemId);
        return ItemMapper.INSTANCE.itemToDto(item);
    }

    @Override
    public List<ItemDto> getAllItemsByUser(Long userId) {
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : itemStorage.getAllItemsByUser(userId)) {
            itemDtos.add(ItemMapper.INSTANCE.itemToDto(item));
        }
        return itemDtos;
    }

    @Override
    public List<ItemDto> searchAvailableItems(String searchText) {
        List<ItemDto> searchResults = new ArrayList<>();
        for (Item item : itemStorage.searchAvailableItems(searchText)) {
            searchResults.add(ItemMapper.INSTANCE.itemToDto(item));
        }
        return searchResults;
    }

}
