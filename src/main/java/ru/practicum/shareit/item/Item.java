package ru.practicum.shareit.item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.request.ItemRequest;

@Data
@EqualsAndHashCode
public class Item {
    Long id;
    String name;
    String description;
    Boolean available;
    Long owner;
    ItemRequest request;
}
