package ru.yandex.practicum.filmorate.feature.film.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import ru.yandex.practicum.filmorate.feature.genre.domain.Genre;
import ru.yandex.practicum.filmorate.feature.mpa.domain.MpaRating;

@Data
public class FilmDto {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
    private MpaRating mpa;
    private List<Genre> genres;
}
