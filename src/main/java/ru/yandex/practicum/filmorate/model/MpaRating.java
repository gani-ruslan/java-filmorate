package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MPA Rating.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MpaRating {
    @NotNull(message = "MPA id is required.")
    private Integer id;

    @NotBlank(message = "MPA name must not be empty.")
    @Size(max = 10, message = "MPA name must be 10 chars max.")
    private String name;

    public MpaRating(MpaRating other) {
        this.id = other.getId();
        this.name = other.getName();
    }
}
