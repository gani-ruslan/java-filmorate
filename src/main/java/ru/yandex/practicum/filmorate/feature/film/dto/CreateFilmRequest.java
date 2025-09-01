package ru.yandex.practicum.filmorate.feature.film.dto;

import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.feature.film.validation.annotation.MinDate;
import ru.yandex.practicum.filmorate.feature.genre.domain.Genre;
import ru.yandex.practicum.filmorate.feature.mpa.domain.MpaRating;

@Data
public class CreateFilmRequest implements FilmPayload {
    @NotBlank(message = "Name must not be empty.")
    private String name;

    @Size(max = 200, message = "Description must be 200 chars max.")
    private String description;

    @MinDate("1895-12-28")
    private LocalDate releaseDate;

    @Positive(message = "Duration must be positive.")
    private Long duration;

    @NotNull
    private MpaRating mpa;

    private List<Genre> genres;
}
