package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserInMemoryStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private Long currentUserId = 1L;

    @Override
    public User addUser(User user) {
        if (users.values().stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new AlreadyExistsException("User with this email already exists.");
        }
        Long userId = generateUserId();
        user.setId(userId);

        users.put(userId, user);

        return user;
    }

    @Override
    public User updateUser(Long userId, User updatedUser) {
        User existingUser = findUserById(userId);

        if (isEmailTakenByOtherUser(userId, updatedUser.getEmail())) {
            throw new AlreadyExistsException("User with this email already exists.");
        }

        String name = updatedUser.getName();
        if (name != null && !name.isBlank()) {
            existingUser.setName(name);
        }

        String email = updatedUser.getEmail();
        if (email != null && !email.isBlank()) {
            existingUser.setEmail(email);
        }

        return users.put(userId, existingUser);
    }

    @Override
    public void deleteUser(Long userId) {
        if (users.remove(userId) == null) {
            throw new NotFoundException("User with ID " + userId + " not found");
        }
    }

    @Override
    public User findUserById(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new NotFoundException("User with ID " + userId + " not found");
        }
        return user;

    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private synchronized Long generateUserId() {
        return currentUserId++;
    }

    private boolean isEmailTakenByOtherUser(Long userId, String email) {
        // Check if there is another user with the same email address
        return users.values().stream()
                .anyMatch(user -> !user.getId().equals(userId) && user.getEmail().equals(email));
    }

}
