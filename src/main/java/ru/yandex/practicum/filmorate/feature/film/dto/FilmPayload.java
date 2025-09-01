package ru.yandex.practicum.filmorate.feature.film.dto;

import java.time.LocalDate;
import java.util.List;
import ru.yandex.practicum.filmorate.feature.genre.domain.Genre;
import ru.yandex.practicum.filmorate.feature.mpa.domain.MpaRating;

public interface FilmPayload {
    String getName();

    String getDescription();

    LocalDate getReleaseDate();

    Long getDuration();

    MpaRating getMpa();

    List<Genre> getGenres();
}
