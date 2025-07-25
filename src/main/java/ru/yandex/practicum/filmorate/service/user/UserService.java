package ru.yandex.practicum.filmorate.service.user;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User addFriend(Long userId, Long friendId) {
        User user = getUserOrThrow(userId);
        User friend = getUserOrThrow(friendId);

        friend.addFriend(user.getId());
        user.addFriend(friend.getId());

        userStorage.updateUser(user);
        userStorage.updateUser(friend);

        return new User(user);
    }

    public User removeFriend(Long userId, Long friendId) {
        User user = getUserOrThrow(userId);
        User friend = getUserOrThrow(friendId);

        friend.removeFriend(user.getId());
        user.removeFriend(friend.getId());

        userStorage.updateUser(user);
        userStorage.updateUser(friend);

        return new User(user);
    }

    public List<User> getFriends(Long userId) {
        User user = getUserOrThrow(userId);

        return user.getFriends().stream()
                .map(userStorage::getUserById)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userIdA, Long userIdB) {
        User userA = getUserOrThrow(userIdA);
        User userB = getUserOrThrow(userIdB);

        Set<Long> commonIds = new HashSet<>(userA.getFriends());
        commonIds.retainAll(userB.getFriends());

        return commonIds.stream()
                .map(userStorage::getUserById)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }


    private User getUserOrThrow(Long id) {
        return userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
    }
}
