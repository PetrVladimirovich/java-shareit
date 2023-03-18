package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.exception.users.UserServiceException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(@Qualifier("dbStorage") UserRepository repository, UserMapper userMapper) {
        this.repository = repository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto create(UserDto dto) {
        if (StringUtils.isBlank(dto.getEmail()) || StringUtils.isBlank(dto.getName())) {
            log.warn("UserServiceImpl.create({}(UserDto)) : UserServiceException(\"the name and/or description fields are not filled in\")", dto);
            throw new UserServiceException("the name and/or description fields are not filled in");
        }
        log.info("UserServiceImpl.create({}(UserDto)) : DONE", dto);
        return userMapper.toDto(repository.create(userMapper.toUser(dto)));
    }

    @Override
    public UserDto update(Long userId, UserDto dto) {
        User user = userMapper.toUser(dto);
        user.setId(userId);
        log.info("UserServiceImpl.update({}(userId), {}(UserDto)) : DONE", userId, dto);
        return userMapper.toDto(repository.save(user));
    }

    @Override
    public UserDto getById(Long userId) {
        log.info("UserServiceImpl.getById({}(userId)) : DONE", userId);
        return userMapper.toDto(repository.getById(userId));
    }

    @Override
    public void deleteById(Long userId) {
        log.info("UserServiceImpl.deleteById({}(userId)) : DONE", userId);
        repository.deleteById(userId);
    }

    @Override
    public List<UserDto> getAll() {
        log.info("UserServiceImpl.getAll() : DONE");
        return userMapper.toUserDto(repository.findAll());
    }
}