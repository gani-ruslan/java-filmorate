package ru.yandex.practicum.filmorate.infrastructure.jdbc.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.config.JdbcRowMapperConfig;
import ru.yandex.practicum.filmorate.feature.film.domain.Film;
import ru.yandex.practicum.filmorate.feature.mpa.domain.MpaRating;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.film.repository.FilmJdbcRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
@Import({JdbcRowMapperConfig.class, FilmJdbcRepository.class})
class FilmJdbcRepositoryTest {

    @Autowired
    private FilmJdbcRepository films;

    @Autowired
    private NamedParameterJdbcOperations jdbc;

    @BeforeEach
    void seedReferenceData() {
        // mpa_rating must exist for FK
        jdbc.update("INSERT INTO mpa_rating(id, name) VALUES (:id, :name) ON CONFLICT DO NOTHING",
                new MapSqlParameterSource().addValue("id", 1).addValue("name", "G"));
        // minimal users for like tests
        jdbc.update("INSERT INTO users(id, email, login, name, birthday) VALUES (:id, :e, :l, :n, :b) ON CONFLICT DO NOTHING",
                new MapSqlParameterSource()
                    .addValue("id", 1L)
                    .addValue("e", "a@a.com")
                    .addValue("l", "a")
                    .addValue("n", "A")
                    .addValue("b", LocalDate.of(1990, 1, 1))
        );
    }

    private Film newFilm(String name) {
        Film f = new Film();
        f.setName(name);
        f.setDescription("desc");
        f.setReleaseDate(LocalDate.of(2000,1,1));
        f.setDuration(100L);
        f.setMpa(new MpaRating(1, null));
        return f;
    }

    @Test
    void createAndFindById() {
        Film saved = films.create(newFilm("F1"));
        Optional<Film> found = films.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("F1", found.get().getName());
        assertNotNull(found.get().getMpa());
        assertEquals(1, found.get().getMpa().getId().intValue());
    }

    @Test
    void updateThenReadBack() {
        Film saved = films.create(newFilm("Old"));
        saved.setName("New");
        saved.setDuration(150L);
        Film updated = films.update(saved);

        Optional<Film> found = films.findById(updated.getId());
        assertTrue(found.isPresent());
        assertEquals("New", found.get().getName());
        assertEquals(150L, found.get().getDuration());
    }

    @Test
    void likeUnlikeAndPopular() {
        Film f1 = films.create(newFilm("A"));
        Film f2 = films.create(newFilm("B"));

        // like f1 by user id 1
        films.like(f1.getId(), 1L);

        List<Film> top = films.findPopular(10);
        assertFalse(top.isEmpty());
        assertEquals(f1.getId(), top.getFirst().getId());

        films.unlike(f1.getId(), 1L);
        List<Film> top2 = films.findPopular(10);
        assertFalse(top2.isEmpty());
        // порядок после удаления лайка может стать по id
        assertTrue(top2.stream().anyMatch(x -> x.getId().equals(f1.getId())));
    }

    @Test
    void deleteByIdRemovesFilm() {
        Film saved = films.create(newFilm("ToDelete"));
        boolean removed = films.deleteById(saved.getId());
        assertTrue(removed);
        assertTrue(films.findById(saved.getId()).isEmpty());
    }
}
