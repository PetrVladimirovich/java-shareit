package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> findAllUsers();
    Optional<User> findUserById(Long userId);
    Optional<User> findUserByEmail(String email);
    User createUser(User user);
    User updateUser(Long userId, User user);
    void deleteUserById(Long userId);
    boolean userExists(Long userId);
}