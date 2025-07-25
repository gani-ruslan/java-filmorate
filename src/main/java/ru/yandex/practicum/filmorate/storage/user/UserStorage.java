package ru.yandex.practicum.filmorate.storage.user;

import java.util.List;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {

    Optional<User> getUserById(Long id);

    User addUser(User user);

    void removeUser(User user);

    User updateUser(User user);

    List<User> findAll();
}
