package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.Item;

import java.util.List;

public interface ItemStorage {
    Item addItem(Item item);

    Item updateItem(Long itemId, Item updatedItem, Long userId);

    Item findItemById(Long itemId);

    List<Item> getAllItemsByUser(Long userId);

    List<Item> searchAvailableItems(String search);

}