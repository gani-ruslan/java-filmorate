package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    public User save(User user) {
        return users.save(user);
    }

    public Optional<User> findById(long id) {
        return users.findById(id);
    }

    public List<User> findAll() {
        return users.findAll();
    }

    public boolean delete(long id) {
        return users.deleteById(id);
    }

    public void addFriend(long userId, long friendId) {
        isUsersPairExist(userId, friendId);
        users.addFriend(userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        isUsersPairExist(userId, friendId);
        users.removeFriend(userId, friendId);
    }

    public List<User> friends(long userId) {
        return users.findFriends(userId);
    }

    public List<User> findFriends(long userId) {
        isUserExist(userId);
        return users.findFriends(userId);
    }

    public List<User> findCommonFriends(long userId, long otherId) {
        HashMap<Long, User> pair = new HashMap<>();
        for (User user : users.findFriends(userId)) {
            pair.put(user.getId(), user);
        }

        ArrayList<User> common = new ArrayList<>();
        for (User user : users.findFriends(otherId)) {
            if (pair.containsKey(user.getId())) {
                common.add(pair.get(user.getId()));
            }
        }
        return common;
    }

    private void isUsersPairExist(long userId, long friendId) {
        users.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found"));

        users.findById(friendId)
                .orElseThrow(() -> new NotFoundException("User with id: " + friendId + " not found"));
    }

    private void isUserExist(long userId) {
        users.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found"));
    }
}
