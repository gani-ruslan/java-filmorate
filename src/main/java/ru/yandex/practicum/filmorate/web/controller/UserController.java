package ru.yandex.practicum.filmorate.web.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.web.dto.UserDto;
import ru.yandex.practicum.filmorate.web.mapper.UserDtoMapper;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserDtoMapper mapper;

    public UserController(UserService userService, UserDtoMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto dto) {
        return mapper.toDto(userService.save(mapper.toDomain(dto)));
    }

    @PutMapping
    public UserDto update(@Valid @RequestBody UserDto dto) {
        return mapper.toDto(userService.save(mapper.toDomain(dto)));
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable long id) {
        return userService.findById(id)
                .map(mapper::toDto)
                .orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        if (!userService.delete(id)) {
            throw new RuntimeException("User not found: " + id);
        }
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public UserDto addFriend(@PathVariable long userId, @PathVariable long friendId) {
        userService.addFriend(userId, friendId);
        return userService.findById(userId)
                .map(mapper::toDto)
                .orElseThrow();
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public UserDto removeFriend(@PathVariable long userId, @PathVariable long friendId) {
        userService.removeFriend(userId, friendId);
        return userService.findById(userId)
                .map(mapper::toDto)
                .orElseThrow();
    }

    @GetMapping("/{userId}/friends")
    public List<UserDto> getFriendList(@PathVariable long userId) {
        return userService.findFriends(userId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<UserDto> getCommonFriendList(@PathVariable long userId, @PathVariable long otherId) {
        return userService.findCommonFriends(userId, otherId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
