package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    private final Long userId = 1L;

    private final User user = new User(
            userId,
            "userName",
            "test@gmail.com");

    private final UserDto userDto = UserMapper.INSTANCE.toUserDto(user);

    @Test
    void getAllUsers() {
        List<UserDto> users = (List.of(userDto));

        when(userRepository.findAll()).thenReturn(List.of(user));

        assertEquals(users, userService.getAllUsers());
    }

    @Test
    void getUserById() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        assertEquals(userDto, userService.getUserById(userId));
    }

    @Test
    void getByUserIdShouldThrowEntityNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getUserById(userId));
    }


    @Test
    void deleteUser() {
        assertDoesNotThrow(() -> userService.deleteUser(1L));
    }

}