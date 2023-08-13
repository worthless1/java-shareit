package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ItemInMemoryStorage implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    private Long currentItemId = 1L;

    @Override
    public Item addItem(Item item) {
        Long itemId = generateItemId();
        item.setId(itemId);
        items.put(itemId, item);

        return item;
    }

    @Override
    public Item updateItem(Long itemId, Item updatedItem, Long userId) {
        Item existingItem = findItemById(itemId);


        String name = updatedItem.getName();
        if (name != null && !name.isBlank()) {
            existingItem.setName(name);
        }

        String description = updatedItem.getDescription();
        if (description != null && !description.isBlank()) {
            existingItem.setDescription(description);
        }

        Boolean available = updatedItem.getAvailable();
        if (available != null) {
            existingItem.setAvailable(available);
        }

        return items.put(itemId, existingItem);
    }

    @Override
    public Item findItemById(Long itemId) {
        Item item = items.get(itemId);
        if (item == null) {
            throw new NotFoundException("Item with ID " + itemId + " not found");
        }
        return item;
    }

    @Override
    public List<Item> getAllItemsByUser(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchAvailableItems(String searchText) {

        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> containsIgnoreCase(item.getName(), searchText)
                        || containsIgnoreCase(item.getDescription(), searchText))
                .collect(Collectors.toList());
    }

    private boolean containsIgnoreCase(String source, String target) {
        return source.toLowerCase().contains(target.toLowerCase()) && !target.isBlank();
    }

    private synchronized Long generateItemId() {
        return currentItemId++;
    }

}
