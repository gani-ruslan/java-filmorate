package ru.yandex.practicum.filmorate.infrastructure.jdbc.user.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.common.exception.NotFoundException;
import ru.yandex.practicum.filmorate.feature.user.domain.User;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.core.BaseJdbcRepository;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.user.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.feature.user.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class UserJdbcRepository extends BaseJdbcRepository<User, Long> implements UserRepository {

    public UserJdbcRepository(NamedParameterJdbcTemplate jdbc,
                              UserRowMapper userRowMapper) {
        super(jdbc, userRowMapper);
    }

    private static final class Sql {
        static final String INSERT = """
            INSERT INTO users (email, login, name, birthday)
            VALUES (:email, :login, :name, :birthday)
            """;

        static final String UPDATE = """
            UPDATE users
            SET email = :email, login = :login, name = :name, birthday = :birthday
            WHERE id = :id
            """;

        static final String SELECT_BY_ID = "SELECT * FROM users WHERE id = :id";

        static final String SELECT_ALL = "SELECT * FROM users ORDER BY id";

        static final String DELETE = "DELETE FROM users WHERE id = :id";

        static final String ADD_FRIEND = """
            INSERT INTO friendships (user_id, friend_id)
            VALUES (:userId, :friendId)
            ON CONFLICT DO NOTHING
            """;

        static final String REMOVE_FRIEND = """
            DELETE FROM friendships WHERE user_id = :userId AND friend_id = :friendId
            """;

        static final String FIND_FRIENDS = """
            SELECT u.* FROM friendships f
            JOIN users u ON u.id = f.friend_id
            WHERE f.user_id = :userId
            ORDER BY u.id
            """;
    }

    /* Default repository implementation */
    @Override
    public List<User> findAll() {
        return queryMany(Sql.SELECT_ALL);
    }

    @Override
    public Optional<User> findById(Long id) {
        return queryOne(Sql.SELECT_BY_ID, Map.of("id", id));
    }

    @Override
    public User create(User user) {
        MapSqlParameterSource params = toParams(user);
        Long id = createEntry(Sql.INSERT, params).orElseThrow(() ->
                new IllegalStateException("User not created."));

        return findById(id).orElseThrow(() ->
                new IllegalStateException("User created, but can't find in DB. id=" + id));
    }

    @Override
    public User update(User user) {
        MapSqlParameterSource params = toParams(user);
        params.addValue("id", user.getId());

        if (execUpdate(Sql.UPDATE, params) == 0) {
            throw new NotFoundException("Entry not found to update id=" + user.getId());
        }

        return findById(user.getId()).orElseThrow(() ->
                new IllegalStateException("User updated, but can't find in DB. id=" + user.getId()));
    }

    @Override
    public boolean deleteById(Long id) {
        return execUpdate(Sql.DELETE, Map.of("id", id)) > 0;
    }

    @Override
    protected Long convert(Number data) {
        return data.longValue();
    }

    /* User repository implementation */
    @Override
    public User addFriend(Long userId, Long friendId) {
        execUpdate(
                Sql.ADD_FRIEND,
                Map.of("userId", userId, "friendId", friendId)
        );
        return findById(userId).orElseThrow(() ->
                new IllegalStateException("User friend added, but can't find in DB. id=" + userId));
    }

    @Override
    public User removeFriend(Long userId, Long friendId) {
        execUpdate(
                Sql.REMOVE_FRIEND,
                Map.of("userId", userId, "friendId", friendId)
        );
        return findById(userId).orElseThrow(() ->
                new IllegalStateException("User friend deleted, but can't find in DB. id=" + userId));
    }

    @Override
    public List<User> findFriends(Long userId) {
        return queryMany(
                Sql.FIND_FRIENDS,
                Map.of("userId", userId)
        );
    }

    /* Utils method */
    private MapSqlParameterSource toParams(User user) {
        return new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday()
                );
    }
}
