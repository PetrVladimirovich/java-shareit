package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ru.practicum.shareit.exception.users.UserRepositoryException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.IdGenerator;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final IdGenerator idGenerator;

    @Override
    public User create(User user) {
        isEmailExist(user.getEmail())
                .ifPresent((u) -> {
                    throw new UserRepositoryException(user.getEmail() + ": this box has already been used");
                });
        long id = idGenerator.getId();
        user.setId(id);
        users.put(id, user);
        log.info("A new user has been added: {}", user.toString());
        return users.get(id);
    }

    @Override
    public User save(User user) {
        User updatedUser = getById(user.getId());
        if (StringUtils.isNotBlank(user.getEmail())) {
            isEmailExist(user.getEmail())
                    .ifPresent(u -> {
                        if (!u.getId().equals(updatedUser.getId())) {
                            throw new UserRepositoryException(user.getEmail() + ": this box has already been used");
                        }
                    });
            updatedUser.setEmail(user.getEmail());
        }
        if (StringUtils.isNotBlank(user.getName())) {
            updatedUser.setName(user.getName());
        }
        log.info("User data updated: {}", updatedUser.toString());
        return users.get(updatedUser.getId());
    }

    @Override
    public User getById(Long userId) {
        return Optional.ofNullable(users.get(userId))
                .orElseGet(() -> {
                    throw new UserRepositoryException(userId + ": this id not found");
                });
    }

    @Override
    public void deleteById(Long userId) {
        User removedUser = users.remove(userId);
        if (removedUser != null) {
            log.info("User deleted: {}", removedUser.toString());
        }
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    private Optional<User> isEmailExist(String email) {
        return users.values().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }
}