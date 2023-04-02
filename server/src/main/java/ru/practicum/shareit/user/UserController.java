package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto dto) {
        log.info("UserController : POST /users");
        return new ResponseEntity<>(userService.create(dto), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long userId, @RequestBody UserDto dto) {
        log.info("UserController : PATCH /users/{}", userId);
        return new ResponseEntity<>(userService.update(userId, dto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long userId) {
        log.info("UserController : GET /users/{}", userId);
        return new ResponseEntity<>(userService.getById(userId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable("id") Long userId) {
        log.info("UserController : DELETE /users/{}", userId);
        userService.deleteById(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("UserController : GET /users");
        return userService.getAll();
    }
}