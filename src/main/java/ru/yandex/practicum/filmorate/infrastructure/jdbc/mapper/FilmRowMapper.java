package ru.yandex.practicum.filmorate.infrastructure.jdbc.mapper;

import static ru.yandex.practicum.filmorate.infrastructure.jdbc.mapper.JdbcReaders.getLocalDate;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(getLocalDate(resultSet, "release_date"));
        film.setDuration(resultSet.getLong("duration"));
        film.setMpaRating(resultSet.getObject("mpa_rating_id") != null
                ? new MpaRating(resultSet.getInt("mpa_rating_id"),
                                resultSet.getString("mpa_rating_name"))
                : null);
        film.setGenres(new ArrayList<>());
        film.setLikes(new HashSet<>());

        return film;
    }
}
