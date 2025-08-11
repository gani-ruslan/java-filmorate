package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Genre.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    @NotNull(message = "Genre id is required.")
    private Integer id;

    @NotBlank(message = "Genre name must not be empty.")
    @Size(max = 50, message = "Genre name must be 50 chars max.")
    private String name;

    public Genre(Genre other) {
        this.id = other.getId();
        this.name = other.getName();
    }
}
