package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
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
    private Integer id;

    @NotBlank(message = "Name must not be empty.")
    private String name;

    @Size(max = 200, message = "Description must be 200 chars max.")
    private String description;

    @MinDate("1895-12-28")
    private LocalDate releaseDate;

    @Positive(message = "Duration must be positive.")
    private long duration;

    public Film(Film film) {
        this.id = film.getId();
        this.name = film.getName();
        this.description = film.getDescription();
        this.releaseDate = film.getReleaseDate();
        this.duration = film.getDuration();
    }
}