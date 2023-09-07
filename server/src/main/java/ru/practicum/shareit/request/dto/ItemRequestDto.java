package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
public class ItemRequestDto {

    private Long id;
    private String description;
    private Instant created = Instant.now();
    private Set<ItemDto> items = new HashSet<>();

}
