package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.alreadyExists.UserAlreadyExistsByEmailException;
import ru.practicum.shareit.exception.alreadyExists.UserAlreadyExistsException;
import ru.practicum.shareit.exception.notFound.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRep;

    @Override
    public List<User> getAllUsers() {
        log.debug("UserService: done getAllUsers.");
        return userRep.getAllUsers();
    }

    @Override
    public User getUserById(Long id) {
        User user = userRep.getUserById(id)
                        .orElseThrow(() -> new UserNotFoundException(id));
        log.debug("UserService: done getUserById - {}.", user);
        return user;
    }

    @Override
    public User createUser(User user) {
        if (user.getId() != null && userRep.userExists(user.getId())) {
            throw new UserAlreadyExistsException(user.getId());
        }
        if (userRep.getAllUsers().contains(user)) {
            throw new UserAlreadyExistsByEmailException(user.getEmail());
        }
        user = userRep.createUser(user);
        log.debug("UserService: done createUser - {}.", user);
        return user;
    }

    @Override
    public User updateUser(Long userId, User user) {
        if (!userRep.userExists(userId)) {
            throw new UserNotFoundException(userId);
        }

        User oldUser = getUserById(userId);
        Optional<User> emailUser = userRep.getUserByEmail(user.getEmail());

        if (emailUser.isPresent() && !emailUser.get().getId().equals(userId)) {
            throw new UserAlreadyExistsByEmailException(user.getEmail());
        }
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }
        log.debug("UserService: done updateUser - {}.", user);
        return userRep.updateUser(userId, oldUser);
    }

    @Override
    public void deleteUserById(Long userId) {
        if (!userRep.userExists(userId)) {
            throw new UserNotFoundException(userId);
        }
        userRep.deleteUserById(userId);
        log.debug("UserService: done deleteUserById - ID {}.", userId);
    }
}