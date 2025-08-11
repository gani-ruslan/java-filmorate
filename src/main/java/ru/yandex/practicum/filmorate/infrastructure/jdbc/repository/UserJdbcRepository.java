package ru.yandex.practicum.filmorate.infrastructure.jdbc.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class UserJdbcRepository implements UserRepository {

    private final NamedParameterJdbcOperations jdbc;
    private final UserRowMapper userMapper;

    public UserJdbcRepository(NamedParameterJdbcOperations jdbc,
                              UserRowMapper userMapper) {
        this.jdbc = jdbc;
        this.userMapper = userMapper;
    }

    private static final class Sql {
        static final String INSERT = """
            INSERT INTO users (email, login, name, birthday)
            VALUES (:email, :login, :name, :birthday)
            """;

        static final String UPDATE = """
            UPDATE users SET email=:email, login=:login, name=:name, birthday=:birthday
            WHERE id=:id
            """;

        static final String SELECT_BY_ID = "SELECT * FROM users WHERE id=:id";

        static final String SELECT_ALL = "SELECT * FROM users ORDER BY id";

        static final String DELETE = "DELETE FROM users WHERE id=:id";

        static final String ADD_FRIEND = """
            INSERT INTO friendships (user_id, friend_id)
            VALUES (:userId, :friendId)
            ON CONFLICT DO NOTHING
            """;

        static final String REMOVE_FRIEND = """
            DELETE FROM friendships WHERE user_id=:userId AND friend_id=:friendId
            """;

        static final String FIND_FRIENDS = """
            SELECT u.* FROM friendships f
            JOIN users u ON u.id = f.friend_id
            WHERE f.user_id=:userId
            ORDER BY u.id
            """;
    }

    @Override
    public User save(User user) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday());

        if (user.getId() == null || user.getId() == 0L) {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbc.update(Sql.INSERT, mapSqlParameterSource, keyHolder, new String[]{"id"});
            long id = keyHolder.getKey().longValue();
            return findById(id).orElseThrow();
        } else {

            mapSqlParameterSource.addValue("id", user.getId());

            int rows = jdbc.update(Sql.UPDATE, mapSqlParameterSource);
            if (rows == 0) {
                throw new NotFoundException("User not found: " + user.getId());
            }

            return findById(user.getId()).orElseThrow();
        }
    }

    @Override
    public Optional<User> findById(long id) {
        List<User> list = jdbc.query(Sql.SELECT_BY_ID, Map.of("id", id), userMapper);
        return list.stream().findFirst();
    }

    @Override
    public List<User> findAll() {
        return jdbc.query(Sql.SELECT_ALL, userMapper);
    }

    @Override public boolean deleteById(long id) {
        return jdbc.update(Sql.DELETE, Map.of("id", id)) > 0;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        jdbc.update(Sql.ADD_FRIEND, Map.of("userId", userId, "friendId", friendId));
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        jdbc.update(Sql.REMOVE_FRIEND, Map.of("userId", userId, "friendId", friendId));
    }

    @Override
    public List<User> findFriends(long userId) {
        return jdbc.query(Sql.FIND_FRIENDS, Map.of("userId", userId), userMapper);
    }
}
