package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
public class Booking {
    int id;
    LocalDateTime start;
    LocalDateTime end;
    Item item;
    User booker;
    Status status;
}

