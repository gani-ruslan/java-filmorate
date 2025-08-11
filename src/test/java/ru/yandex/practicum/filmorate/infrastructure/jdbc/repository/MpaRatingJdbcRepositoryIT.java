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
import ru.yandex.practicum.filmorate.model.MpaRating;

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
@Import({MapperConfig.class, MpaRatingJdbcRepository.class})
class MpaRatingJdbcRepositoryIT {

    @Autowired
    private MpaRatingJdbcRepository mpa;

    @Autowired
    private NamedParameterJdbcOperations jdbc;

    @BeforeEach
    void seed() {
        jdbc.update("INSERT INTO mpa_rating(id, name) VALUES (1,'G'),(2,'PG') ON CONFLICT DO NOTHING",
                new MapSqlParameterSource());
    }

    @Test
    void findAllReturnsSeeded() {
        List<MpaRating> all = mpa.findAll();
        assertFalse(all.isEmpty());
    }

    @Test
    void findByIdWorks() {
        Optional<MpaRating> g = mpa.findById(1);
        assertTrue(g.isPresent());
        assertEquals("G", g.get().getName());
    }
}
