package ru.yandex.practicum.filmorate.infrastructure.jdbc.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class FilmJdbcRepository implements FilmRepository {

    private final NamedParameterJdbcOperations jdbc;
    private final FilmRowMapper filmMapper;

    public FilmJdbcRepository(NamedParameterJdbcOperations jdbc,
                              FilmRowMapper filmMapper) {
        this.jdbc = jdbc;
        this.filmMapper = filmMapper;
    }

    private static final class Sql {
        static final String INSERT = """
            INSERT INTO films (name, description, release_date, duration, mpa_rating_id)
            VALUES (:name, :description, :releaseDate, :duration, :mpaId)
            """;

        static final String UPDATE = """
            UPDATE films SET name=:name, description=:description,
                             release_date=:releaseDate, duration=:duration, mpa_rating_id=:mpaId
            WHERE id=:id
            """;

        static final String SELECT_BY_ID = """
            SELECT f.id, f.name, f.description, f.release_date, f.duration,
                   m.id AS mpa_rating_id, m.name AS mpa_rating_name
            FROM films f
            LEFT JOIN mpa_rating m ON m.id = f.mpa_rating_id
            WHERE f.id=:id
            """;

        static final String SELECT_ALL = """
            SELECT f.id, f.name, f.description, f.release_date, f.duration,
                   m.id AS mpa_rating_id, m.name AS mpa_rating_name
            FROM films f
            LEFT JOIN mpa_rating m ON m.id = f.mpa_rating_id
            ORDER BY f.id
            """;

        static final String DELETE = "DELETE FROM films WHERE id=:id";

        static final String LIKE = """
            INSERT INTO film_likes (film_id, user_id)
            VALUES (:filmId, :userId)
            ON CONFLICT DO NOTHING
            """;

        static final String UNLIKE = """
            DELETE FROM film_likes WHERE film_id=:filmId AND user_id=:userId
            """;

        static final String POPULAR = """
            SELECT f.id, f.name, f.description, f.release_date, f.duration,
                   m.id AS mpa_rating_id, m.name AS mpa_rating_name,
                   COUNT(fl.user_id) AS likes
            FROM films f
            JOIN mpa_rating m ON m.id = f.mpa_rating_id
            LEFT JOIN film_likes fl ON fl.film_id = f.id
            GROUP BY f.id, f.name, f.description, f.release_date, f.duration, m.id, m.name
            ORDER BY likes DESC, f.id
            LIMIT :limit
            """;
    }

    @Override
    public Film save(Film film) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("releaseDate", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("mpaId", film.getMpaRating() != null ? film.getMpaRating().getId() : null);

        if (film.getId() == null || film.getId() == 0L) {

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbc.update(Sql.INSERT, mapSqlParameterSource, keyHolder, new String[]{"id"});
            long id = keyHolder.getKey().longValue();

            return findById(id).orElseThrow();

        } else {

            mapSqlParameterSource.addValue("id", film.getId());

            int rows = jdbc.update(Sql.UPDATE, mapSqlParameterSource);
            if (rows == 0) {
                throw new NotFoundException("Film not found: " + film.getId());
            }

            return findById(film.getId()).orElseThrow();
        }
    }

    @Override
    public Optional<Film> findById(long id) {
        List<Film> list = jdbc.query(Sql.SELECT_BY_ID, Map.of("id", id), filmMapper);
        return list.stream().findFirst();
    }

    @Override
    public List<Film> findAll() {
        return jdbc.query(Sql.SELECT_ALL, filmMapper);
    }

    @Override
    public boolean deleteById(long id) {
        return jdbc.update(Sql.DELETE, Map.of("id", id)) > 0;
    }

    @Override
    public void like(long filmId, long userId) {
        jdbc.update(Sql.LIKE,   Map.of("filmId", filmId, "userId", userId));
    }

    @Override
    public void unlike(long filmId, long userId) {
        jdbc.update(Sql.UNLIKE, Map.of("filmId", filmId, "userId", userId));
    }

    @Override public List<Film> findPopular(int limit) {
        return jdbc.query(Sql.POPULAR, Map.of("limit", limit), filmMapper);
    }
}
