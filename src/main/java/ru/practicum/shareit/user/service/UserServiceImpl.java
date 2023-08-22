package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return UserMapper.INSTANCE.convertListOfUserToUserDTOList(userRepository.findAll());
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = getUserIfExists(userId);
        return UserMapper.INSTANCE.toUserDto(user);
    }

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.INSTANCE.toUser(userDto);
        user = userRepository.save(user);
        return UserMapper.INSTANCE.toUserDto(user);
    }

    @Transactional
    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = getUserIfExists(userId);
        updateUserDetails(user, userDto);
        return UserMapper.INSTANCE.toUserDto(userRepository.saveAndFlush(user));
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    private User getUserIfExists(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id " + userId + " not found"));
    }

    private void updateUserDetails(User user, UserDto userDto) {
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
    }

}
