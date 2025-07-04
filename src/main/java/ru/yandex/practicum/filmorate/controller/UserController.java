package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

@RestController
@RequestMapping("/users")
public class UserController {
    private final AtomicInteger userId = new AtomicInteger(0);
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values().stream()
                .map(User::new)
                .toList();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {

        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        User createdUser = new User(user);
        users.put(user.getId(), createdUser);
        return new User(createdUser);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {

            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }

            User updatedUser = new User(user);
            users.put(user.getId(), updatedUser);
            return new User(updatedUser);
        }
        throw new NotFoundException("User with ID: " + user.getId() + " not found.");
    }

    private int getNextId() {
        return userId.incrementAndGet();
    }
}