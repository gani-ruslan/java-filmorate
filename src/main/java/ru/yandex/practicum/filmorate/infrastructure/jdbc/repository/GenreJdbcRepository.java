package ru.yandex.practicum.filmorate.infrastructure.jdbc.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class GenreJdbcRepository implements GenreRepository {

    private final NamedParameterJdbcOperations jdbc;
    private final GenreRowMapper genreMapper;

    public GenreJdbcRepository(NamedParameterJdbcOperations jdbc,
                               GenreRowMapper genreMapper) {
        this.jdbc = jdbc;
        this.genreMapper = genreMapper;
    }

    private static final class Sql {
        static final String SELECT_ALL = "SELECT id, name FROM genres ORDER BY id";

        static final String SELECT_BY_ID = "SELECT id, name FROM genres WHERE id=:id";

        static final String SELECT_BY_FILM = """
            SELECT g.id, g.name
            FROM film_genres fg
            JOIN genres g ON g.id = fg.genre_id
            WHERE fg.film_id = :filmId
            ORDER BY g.id
            """;

        static final String DELETE_FILM_GENRES = "DELETE FROM film_genres WHERE film_id=:filmId";

        static final String INSERT_FILM_GENRE = """
            INSERT INTO film_genres (film_id, genre_id)
            VALUES (:filmId, :genreId)
            ON CONFLICT DO NOTHING
            """;
    }

    @Override
    public List<Genre> findAll() {
        return jdbc.query(Sql.SELECT_ALL, genreMapper);
    }

    @Override
    public Optional<Genre> findById(int id) {
        List<Genre> list = jdbc.query(Sql.SELECT_BY_ID, Map.of("id", id), genreMapper);
        return list.stream().findFirst();
    }

    @Override
    public List<Genre> findByFilmId(long filmId) {
        return jdbc.query(Sql.SELECT_BY_FILM, Map.of("filmId", filmId), genreMapper);
    }

    @Override
    public void setFilmGenres(long filmId,
                              Collection<Integer> genreIds) {
        jdbc.update(Sql.DELETE_FILM_GENRES, Map.of("filmId", filmId));

        if (genreIds == null || genreIds.isEmpty()) {
            return;
        }

        MapSqlParameterSource[] batch = genreIds.stream()
                .map(id -> new MapSqlParameterSource()
                        .addValue("filmId", filmId)
                        .addValue("genreId", id))
                .toArray(MapSqlParameterSource[]::new);

        jdbc.batchUpdate(Sql.INSERT_FILM_GENRE, batch);
    }
}
