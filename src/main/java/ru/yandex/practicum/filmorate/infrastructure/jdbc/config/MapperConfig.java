package ru.yandex.practicum.filmorate.infrastructure.jdbc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.mapper.*;

@Configuration
public class MapperConfig {
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
