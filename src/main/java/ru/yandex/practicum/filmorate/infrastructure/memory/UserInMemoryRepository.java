package ru.yandex.practicum.filmorate.infrastructure.memory;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashSet;
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
    public User save(User user) {
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
    public Optional<User> findById(long id) {
        User stored = users.get(id);
        return stored == null ? Optional.empty() : Optional.of(cloneUser(stored));
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        for (User user : users.values()) list.add(cloneUser(user));
        return list;
    }

    @Override
    public boolean deleteById(long id) {
        return users.remove(id) != null;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        User user = users.get(userId);
        if (user == null) throw new NotFoundException("User not found: " + userId);
        user.getFriends().add(friendId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        User user = users.get(userId);
        if (user == null) throw new NotFoundException("User not found: " + userId);
        user.getFriends().remove(friendId);
    }

    @Override
    public List<User> findFriends(long userId) {
        User user = users.get(userId);
        if (user == null) throw new NotFoundException("User not found: " + userId);
        List<User> result = new ArrayList<>();
        for (Long fid : user.getFriends()) {
            User f = users.get(fid);
            if (f != null) result.add(cloneUser(f));
        }
        return result;
    }

    private User cloneUser(User src) {
        User copy = new User(src);
        if (copy.getFriends() == null) copy.setFriends(new HashSet<>());
        return copy;
    }
}
