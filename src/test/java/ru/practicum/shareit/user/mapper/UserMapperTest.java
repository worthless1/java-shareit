package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperTest {

    private final UserMapperImpl userMapper = new UserMapperImpl();

    @Test
    void toUserDto() {
        UserDto userDto = userMapper.toUserDto(null);

        assertNull(userDto);
    }

    @Test
    void toUser() {
        User user = userMapper.toUser(null);

        assertNull(user);
    }

    @Test
    void convertUserListToUserDTOList() {
        List<UserDto> users = userMapper.convertListOfUserToUserDTOList(null);

        assertNull(users);
    }

}