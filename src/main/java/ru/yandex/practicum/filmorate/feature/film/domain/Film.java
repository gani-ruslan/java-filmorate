package ru.yandex.practicum.filmorate.feature.film.domain;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.feature.genre.domain.Genre;
import ru.yandex.practicum.filmorate.feature.mpa.domain.MpaRating;

/**
 * Film.
 */
@Data
@NoArgsConstructor
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Long duration;
    private MpaRating mpa;
    private List<Genre> genres;

    public Film(Film film) {
        this.id = film.getId();
        this.name = film.getName();
        this.description = film.getDescription();
        this.releaseDate = film.getReleaseDate();
        this.duration = film.getDuration();
        this.mpa = film.getMpa();
        this.genres = film.getGenres();
    }
}