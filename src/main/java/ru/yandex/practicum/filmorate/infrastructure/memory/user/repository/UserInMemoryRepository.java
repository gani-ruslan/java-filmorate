package ru.yandex.practicum.filmorate.infrastructure.memory.user.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.feature.user.domain.User;
import ru.yandex.practicum.filmorate.feature.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("inmemory")
public class UserInMemoryRepository implements UserRepository {

    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        for (User user : users.values()) list.add(cloneUser(user));
        return list;
    }

    @Override
    public Optional<User> findById(Long id) {
        User stored = users.get(id);
        return stored == null ? Optional.empty() : Optional.of(cloneUser(stored));
    }

    @Override
    public User create(User user) {
        if (user.getId() == null || user.getId() == 0L) {
            long id = seq.incrementAndGet();
            user.setId(id);
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), cloneUser(user));
        return cloneUser(users.get(user.getId()));
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), cloneUser(user));
        return cloneUser(users.get(user.getId()));
    }

    @Override
    public boolean deleteById(Long id) {
        return users.remove(id) != null;
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User removeFriend(Long userId, Long friendId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> findFriends(Long userId) {
        throw new UnsupportedOperationException();
    }

    private User cloneUser(User src) {
        return new User(src);
    }
}
