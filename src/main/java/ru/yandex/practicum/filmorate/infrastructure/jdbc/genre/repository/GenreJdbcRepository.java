package ru.yandex.practicum.filmorate.infrastructure.jdbc.genre.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.feature.genre.domain.Genre;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.core.BaseJdbcRepository;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.genre.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.feature.genre.repository.GenreRepository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class GenreJdbcRepository extends BaseJdbcRepository<Genre, Integer> implements GenreRepository {

    public GenreJdbcRepository(NamedParameterJdbcTemplate jdbc,
                               GenreRowMapper genreRowMapper) {
        super(jdbc, genreRowMapper);
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

    /* Default repository implementation */
    @Override
    public List<Genre> findAll() {
        return queryMany(Sql.SELECT_ALL);
    }

    @Override
    public Optional<Genre> findById(Integer id) {
        return queryOne(Sql.SELECT_BY_ID, Map.of("id", id));
    }

    @Override
    public Genre create(Genre genre) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Genre update(Genre genre) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean deleteById(Integer id) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Integer convert(Number data) {
        return data.intValue();
    }

    /* Genre repository implementation */
    @Override
    public List<Genre> getFilmGenres(Long filmId) {
        return queryMany(Sql.SELECT_BY_FILM, Map.of("filmId", filmId));
    }

    @Override
    public void setFilmGenres(Long filmId,
                                 Collection<Integer> genreIds) {

        execUpdate(Sql.DELETE_FILM_GENRES, Map.of("filmId", filmId));

        if (genreIds == null || genreIds.isEmpty()) {
            return;
        }

        MapSqlParameterSource[] genres = genreIds.stream()
                .map(id -> new MapSqlParameterSource()
                                        .addValue("filmId", filmId)
                                        .addValue("genreId", id)
                )
                .toArray(MapSqlParameterSource[]::new);

        execUpdate(Sql.INSERT_FILM_GENRE, genres);
    }
}
