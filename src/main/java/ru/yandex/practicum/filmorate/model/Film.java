package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.MinDate;

/**
 * Film.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private Long id;
    private Set<Long> likes = new HashSet<>();

    @NotBlank(message = "Name must not be empty.")
    private String name;

    @Size(max = 200, message = "Description must be 200 chars max.")
    private String description;

    @MinDate("1895-12-28")
    private LocalDate releaseDate;

    @Positive(message = "Duration must be positive.")
    private long duration;

    @NotNull(message = "MPA rating is required.")
    @Valid
    private MpaRating mpaRating;

    @Valid
    private List<Genre> genres = new ArrayList<>();

    public Film(Film film) {
        this.id = film.getId();
        this.name = film.getName();
        this.description = film.getDescription();
        this.releaseDate = film.getReleaseDate();
        this.duration = film.getDuration();
        this.genres = film.getGenres();
        this.mpaRating = film.getMpaRating();
        this.likes = new HashSet<>(film.getLikes());
    }

    public void likeFilm(Long filmId) {
        likes.add(filmId);
    }

    public void unlikeFilm(Long filmId) {
        likes.remove(filmId);
    }
}