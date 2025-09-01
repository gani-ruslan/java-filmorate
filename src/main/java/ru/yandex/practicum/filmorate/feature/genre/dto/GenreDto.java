package ru.yandex.practicum.filmorate.feature.genre.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GenreDto {
    @NotNull(message = "Genre id is required.")
    private Integer id;

    @NotBlank(message = "Genre name must not be empty.")
    @Size(max = 50, message = "Genre name must be 50 chars max.")
    private String name;
}
