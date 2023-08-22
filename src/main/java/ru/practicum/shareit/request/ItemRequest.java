package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class ItemRequest {
    int id;
    String description;
    User requester;
    LocalDateTime created;
}
