package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.users.UserRepositoryException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Slf4j
@Repository("dbStorage")
public class UserRepositoryImpl implements UserRepository {
    private final UserRepositoryJpa userRepositoryJpa;

    public UserRepositoryImpl(UserRepositoryJpa userRepositoryJpa) {
        this.userRepositoryJpa = userRepositoryJpa;
    }

    @Override
    public User create(User user) {
        User newUser = userRepositoryJpa.save(user);
        log.info("A new user has been added: {}", newUser.toString());
        return newUser;
    }

    @Override
    public User save(User updatedUser) {
        User user = getById(updatedUser.getId());
        if (StringUtils.isNotBlank(updatedUser.getName())) {
            user.setName(updatedUser.getName());
        }
        if (StringUtils.isNotBlank(updatedUser.getEmail())) {
            user.setEmail(updatedUser.getEmail());
        }
        userRepositoryJpa.save(user);
        log.info("User data updated: {}", user.toString());
        return user;
    }

    @Override
    public User getById(Long userId) {
        return userRepositoryJpa.findById(userId)
                .orElseGet(() -> {
                    throw new UserRepositoryException(userId + ": this id not found");
                });
    }

    @Override
    public void deleteById(Long userId) {
        userRepositoryJpa.findById(userId).ifPresent(u -> {
            userRepositoryJpa.deleteById(userId);
            log.info("User deleted: {}", u.toString());
        });
    }

    @Override
    public List<User> findAll() {
        return userRepositoryJpa.findAll();
    }
}