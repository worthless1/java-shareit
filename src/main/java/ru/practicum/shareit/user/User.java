package ru.practicum.shareit.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class User {
    Long id;
    String name;
    String email;
}
