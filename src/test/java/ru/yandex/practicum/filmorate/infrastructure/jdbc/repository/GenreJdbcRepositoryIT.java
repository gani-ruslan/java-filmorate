package ru.yandex.practicum.filmorate.infrastructure.jdbc.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.config.MapperConfig;
import ru.yandex.practicum.filmorate.model.Genre;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("jdbc")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.sql.init.mode=always",
        "spring.datasource.url=jdbc:h2:mem:filmorate_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password="
})
@Import({MapperConfig.class, GenreJdbcRepository.class})
class GenreJdbcRepositoryIT {

    @Autowired
    private GenreJdbcRepository genres;

    @Autowired
    private NamedParameterJdbcOperations jdbc;

    @BeforeEach
    void seed() {
        // reference data for genres and mpa + one film to attach
        jdbc.update("INSERT INTO genres(id, name) VALUES (1,'Comedy'),(2,'Drama') ON CONFLICT DO NOTHING", new MapSqlParameterSource());
        jdbc.update("INSERT INTO mpa_rating(id, name) VALUES (1,'G') ON CONFLICT DO NOTHING", new MapSqlParameterSource());
        jdbc.update("INSERT INTO films(name, description, release_date, duration, mpa_rating_id) " +
                        "VALUES (:n,:d,:rd,:dur,:m)",
                new MapSqlParameterSource()
                        .addValue("n","FilmX")
                        .addValue("d","desc")
                        .addValue("rd", LocalDate.of(2000,1,1))
                        .addValue("dur", 90)
                        .addValue("m", 1));
    }

    private long findFilmId() {
        // simplest: max id
        Long id = jdbc.queryForObject("SELECT MAX(id) FROM films", new MapSqlParameterSource(), Long.class);
        return id != null ? id : 0L;
    }

    @Test
    void findAllReturnsSeeded() {
        List<Genre> all = genres.findAll();
        assertFalse(all.isEmpty());
    }

    @Test
    void setAndGetFilmGenres() {
        long filmId = findFilmId();
        genres.setFilmGenres(filmId, Arrays.asList(1,2));
        List<Genre> attached = genres.findByFilmId(filmId);
        assertEquals(2, attached.size());
        assertEquals(1, attached.get(0).getId().intValue());
        assertEquals(2, attached.get(1).getId().intValue());
    }
}
