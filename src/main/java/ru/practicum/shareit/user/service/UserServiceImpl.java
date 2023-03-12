package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.users.RepeatEmailException;
import ru.practicum.shareit.exception.users.UserNotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto addUser(UserDto userDto) {
        UserDto user = userMapper.toDTO(userRepository.save(userMapper.toModel(userDto)));
        log.info("UserServiceImpl.addUser : DONE");
        return user;
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User userToUpdate = getUser(userId);
        User updateUser = new User();

        if ((userToUpdate.getEmail().equals(userDto.getEmail()) ||
                userRepository.findByEmail(userDto.getEmail()).isEmpty())) {
            updateUser = composeUser(userToUpdate, userDto);
            userRepository.update(updateUser.getName(), updateUser.getEmail(), updateUser.getId());
        } else if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RepeatEmailException(userDto.getEmail());
        }
        log.info("UserServiceImpl.updateUser : DONE");
        return userMapper.toDTO(updateUser);
    }

    @Override
    public User getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        log.info("UserServiceImpl.getUser : DONE");
        return user;
    }

    @Override
    public User composeUser(User user, UserDto userDto) {
        User us = User.builder()
                .id(user.getId())
                .name(userDto.getName() != null ? userDto.getName() : user.getName())
                .email(userDto.getEmail() != null ? userDto.getEmail() : user.getEmail())
                .build();
        log.info("UserServiceImpl.composeUser : DONE");
        return us;
    }
}
