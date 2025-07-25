package ru.yandex.practicum.filmorate.storage.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

/**
 * Service user.
 */
@Component
public class InMemoryUserStorage implements UserStorage {

    private final AtomicInteger userId = new AtomicInteger(0);
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Optional<User> getUserById(Long id) {
        if (id == null || !users.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(new User(users.get(id)));
    }

    @Override
    public List<User> findAll() {
        return users.values().stream()
                .map(User::new)
                .toList();
    }

    @Override
    public User addUser(User user) {
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        User createdUser = new User(user);
        users.put(user.getId(), createdUser);
        return new User(createdUser);
    }

    @Override
    public void removeUser(User user) {
        if (user == null || !users.containsKey(user.getId())) {
            throw new NotFoundException("User to remove not found with ID: "
                    + (user == null ? null : user.getId()));
        }
        users.remove(user.getId());
    }

    @Override
    public User updateUser(User user) {
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

    private Long getNextId() {
        return (long) userId.incrementAndGet();
    }
}
