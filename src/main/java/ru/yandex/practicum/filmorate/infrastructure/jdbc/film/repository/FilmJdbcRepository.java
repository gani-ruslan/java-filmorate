package ru.yandex.practicum.filmorate.infrastructure.jdbc.film.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.common.exception.NotFoundException;
import ru.yandex.practicum.filmorate.feature.film.domain.Film;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.core.BaseJdbcRepository;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.film.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.feature.film.repository.FilmRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class FilmJdbcRepository extends BaseJdbcRepository<Film, Long> implements FilmRepository {

    public FilmJdbcRepository(NamedParameterJdbcTemplate jdbc,
                              FilmRowMapper filmRowMapper) {
        super(jdbc, filmRowMapper);
    }

    private static final class Sql {
        static final String INSERT = """
            INSERT INTO films (name, description, release_date, duration, mpa_rating_id)
            VALUES (:name, :description, :releaseDate, :duration, :mpaId)
            """;

        static final String UPDATE = """
            UPDATE films
            SET name = :name, description = :description, release_date = :releaseDate,
             duration = :duration, mpa_rating_id = :mpaId
            WHERE id = :id
            """;

        static final String SELECT_BY_ID = """
            SELECT f.id, f.name, f.description, f.release_date, f.duration,
                   m.id AS mpa_rating_id, m.name AS mpa_rating_name
            FROM films f
            LEFT JOIN mpa_rating m ON m.id = f.mpa_rating_id
            WHERE f.id = :id
            LIMIT 1
            """;

        static final String SELECT_ALL = """
            SELECT f.id, f.name, f.description, f.release_date, f.duration,
                   m.id AS mpa_rating_id, m.name AS mpa_rating_name
            FROM films f
            LEFT JOIN mpa_rating m ON m.id = f.mpa_rating_id
            ORDER BY f.id
            """;

        static final String DELETE = "DELETE FROM films WHERE id = :id";

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
            LEFT JOIN mpa_rating m ON m.id = f.mpa_rating_id
            LEFT JOIN film_likes fl ON fl.film_id = f.id
            GROUP BY f.id, f.name, f.description, f.release_date, f.duration, m.id, m.name
            ORDER BY likes DESC, f.id
            LIMIT :limit
            """;
    }

    /* Default repository implementation */
    @Override
    public List<Film> findAll() {
        return queryMany(Sql.SELECT_ALL);
    }

    @Override
    public Optional<Film> findById(Long id) {
        return queryOne(Sql.SELECT_BY_ID, Map.of("id", id));
    }

    @Override
    public Film create(Film film) {
        MapSqlParameterSource params = toParams(film);

        Long id = createEntry(Sql.INSERT, params).orElseThrow(() ->
                new IllegalStateException("Film not created."));

        return findById(id).orElseThrow(() ->
                new IllegalStateException("Film created, but can't find in DB. id=" + id));
    }

    @Override
    public Film update(Film film) {
        MapSqlParameterSource params = toParams(film);
        params.addValue("id", film.getId());

        if (execUpdate(Sql.UPDATE, params) == 0) {
            throw new NotFoundException("Entry not found to update id=" + film.getId());
        }

        return findById(film.getId()).orElseThrow(() ->
                new IllegalStateException("Film updated, but can't find in DB. id=" + film.getId()));
    }

    @Override
    public boolean deleteById(Long id) {
        return execUpdate(Sql.DELETE, Map.of("id", id)) > 0;
    }

    @Override
    protected Long convert(Number data) {
        return data.longValue();
    }

    /* Film repository implementation */
    @Override
    public void like(Long filmId, Long userId) {
        jdbc.update(
                Sql.LIKE,
                Map.of(
                        "filmId", filmId,
                        "userId", userId
                )
        );
    }

    @Override
    public void unlike(Long filmId, Long userId) {
        jdbc.update(
                Sql.UNLIKE,
                Map.of(
                        "filmId", filmId,
                        "userId", userId
                )
        );
    }

    @Override
    public List<Film> findPopular(int count) {
        return queryMany(
                Sql.POPULAR,
                Map.of("limit", count)
        );
    }

    /* Utils method */
    private MapSqlParameterSource toParams(Film film) {
        return new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("releaseDate", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("mpaId", film.getMpa() != null
                        ? film.getMpa().getId()
                        : null
                );
    }
}
