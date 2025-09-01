package ru.yandex.practicum.filmorate.infrastructure.jdbc.film.mapper;

import static ru.yandex.practicum.filmorate.infrastructure.jdbc.utils.JdbcColumnReaders.getLocalDate;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.feature.film.domain.Film;
import ru.yandex.practicum.filmorate.feature.mpa.domain.MpaRating;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(getLocalDate(resultSet, "release_date"));
        film.setDuration(resultSet.getLong("duration"));
        film.setMpa(resultSet.getObject("mpa_rating_id") != null
                ? new MpaRating(resultSet.getInt("mpa_rating_id"),
                                resultSet.getString("mpa_rating_name"))
                : null);
        film.setGenres(new ArrayList<>());

        return film;
    }
}
