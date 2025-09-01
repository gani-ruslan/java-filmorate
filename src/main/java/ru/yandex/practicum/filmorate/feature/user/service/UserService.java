package ru.yandex.practicum.filmorate.feature.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.common.exception.NotFoundException;
import ru.yandex.practicum.filmorate.feature.user.domain.User;
import ru.yandex.practicum.filmorate.feature.user.repository.UserRepository;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    public List<User> findAll() {
        return users.findAll();
    }

    public User findById(long id) {
        return users.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    @Transactional
    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        User created = users.create(user);
        return findById(created.getId());
    }

    @Transactional
    public User update(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        User updated = users.update(user);
        return findById(updated.getId());
    }

    @Transactional
    public void delete(long id) {
        if (!users.deleteById(id)) {
            throw new NotFoundException("User not found: " + id);
        }
    }

    public User addFriend(long userId, long friendId) {
        isUserExist(userId);
        isUserExist(friendId);
        users.addFriend(userId, friendId);
        return findById(userId);
    }

    public User removeFriend(long userId, long friendId) {
        isUserExist(userId);
        isUserExist(friendId);
        users.removeFriend(userId, friendId);
        return findById(userId);
    }

    public List<User> findFriends(long userId) {
        isUserExist(userId);
        return users.findFriends(userId);
    }

    public List<User> findCommonFriends(long userId, long friendId) {
        isUserExist(userId);
        isUserExist(friendId);

        Set<Long> friendIds = users.findFriends(userId).stream()
                .map(User::getId)
                .collect(Collectors.toSet());

        return users.findFriends(friendId).stream()
                .filter(u -> friendIds.contains(u.getId()))
                .toList();
    }

    private void isUserExist(long userId) {
        users.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found"));
    }
}
