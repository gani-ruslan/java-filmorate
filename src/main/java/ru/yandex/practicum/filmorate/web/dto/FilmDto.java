package ru.yandex.practicum.filmorate.web.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.validation.MinDate;

@Data
public class FilmDto {
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

    private MpaRating mpa;

    private List<Genre> genres = new ArrayList<>();
}
