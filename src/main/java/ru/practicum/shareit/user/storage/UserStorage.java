package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);

    User updateUser(Long userId, User user);

    void deleteUser(Long userId);

    User findUserById(Long userId);

    List<User> getAllUsers();

}
