package ru.yandex.practicum.filmorate.feature.mpa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MpaRatingDto {
    @NotNull(message = "MPA id is required.")
    private Integer id;

    @NotBlank(message = "MPA name must not be empty.")
    @Size(max = 10, message = "MPA name must be 10 chars max.")
    private String name;
}

