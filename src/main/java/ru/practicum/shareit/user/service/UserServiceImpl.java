package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.INSTANCE.dtoToUser(userDto);
        User createdUser = userStorage.addUser(user);

        return UserMapper.INSTANCE.userToDto(createdUser);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User updatedUser = UserMapper.INSTANCE.dtoToUser(userDto);

        updatedUser = userStorage.updateUser(userId, updatedUser);

        return UserMapper.INSTANCE.userToDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userStorage.deleteUser(userId);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userStorage.findUserById(userId);
        if (user == null) {
            throw new NotFoundException("User with ID " + userId + " not found");
        }

        return UserMapper.INSTANCE.userToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userStorage.getAllUsers();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(UserMapper.INSTANCE.userToDto(user));
        }
        return userDtos;
    }

}
