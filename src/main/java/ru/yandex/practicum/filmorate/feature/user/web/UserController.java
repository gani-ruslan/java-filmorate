package ru.yandex.practicum.filmorate.feature.user.web;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.feature.user.dto.CreateUserRequest;
import ru.yandex.practicum.filmorate.feature.user.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.feature.user.service.UserService;
import ru.yandex.practicum.filmorate.feature.user.dto.UserDto;
import ru.yandex.practicum.filmorate.feature.user.mapper.UserDtoMapper;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserDtoMapper mapper;

    public UserController(UserService userService,
                          UserDtoMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<UserDto> findAll() {
        return mapper.toDto(userService.findAll());
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        return mapper.toDto(userService.findById(id));
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody CreateUserRequest dto) {
        return mapper.toDto(userService.create(mapper.toDomainCreate(dto)));
    }

    @PutMapping
    public UserDto update(@Valid @RequestBody UpdateUserRequest dto) {
        return mapper.toDto(userService.update(mapper.toDomainUpdate(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public UserDto addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        return mapper.toDto(userService.addFriend(userId, friendId));
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public UserDto removeFriend(@PathVariable long userId, @PathVariable Long friendId) {
        return mapper.toDto(userService.removeFriend(userId, friendId));
    }

    @GetMapping("/{userId}/friends")
    public List<UserDto> getFriendList(@PathVariable Long userId) {
        return mapper.toDto(userService.findFriends(userId));
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public List<UserDto> getCommonFriendList(@PathVariable Long userId, @PathVariable Long friendId) {
        return mapper.toDto(userService.findCommonFriends(userId, friendId));
    }
}
