package ru.yandex.practicum.filmorate.feature.user.repository;

import ru.yandex.practicum.filmorate.common.repository.BaseRepository;
import ru.yandex.practicum.filmorate.feature.user.domain.User;
import java.util.List;

public interface UserRepository extends BaseRepository<User, Long> {
    User addFriend(Long userId, Long friendId);

    User removeFriend(Long userId, Long friendId);

    List<User> findFriends(Long userId);
}
