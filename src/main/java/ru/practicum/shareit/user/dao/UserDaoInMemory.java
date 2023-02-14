package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserDaoInMemory implements UserDao {
    private final Map<Long, User> users = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return users.values().stream()
                .filter(user -> userId.equals(user.getId()))
                .findFirst();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public User createUser(User user) {
        if (user.getId() == null) {
            user.setId(nextId++);
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(Long userId, User user) {
        users.replace(userId, user);
        user.setId(userId);
        return user;
    }

    @Override
    public void deleteUserById(Long userId) {
        users.remove(userId);
    }

    @Override
    public boolean userExists(Long userId) {
        return users.containsKey(userId);
    }
}