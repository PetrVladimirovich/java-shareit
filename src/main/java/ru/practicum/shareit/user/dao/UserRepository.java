package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository {

    List<User> getAllUsers();

    Optional<User> getUserById(Long userId);

    Optional<User> getUserByEmail(String email);

    User createUser(User user);

    User updateUser(Long userId, User user);

    void deleteUserById(Long userId);

    boolean userExists(Long userId);

}