package ru.yandex.practicum.filmorate.infrastructure.jdbc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.film.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.genre.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.mpa.mapper.MpaRatingRowMapper;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.user.mapper.UserRowMapper;

@Configuration
public class JdbcRowMapperConfig {
    @Bean
    public FilmRowMapper filmRowMapper() {
        return new FilmRowMapper();
    }

    @Bean
    public UserRowMapper userRowMapper() {
        return new UserRowMapper();
    }

    @Bean
    public GenreRowMapper genreRowMapper() {
        return new GenreRowMapper();
    }

    @Bean
    public MpaRatingRowMapper mpaRatingRowMapper() {
        return new MpaRatingRowMapper();
    }
}
