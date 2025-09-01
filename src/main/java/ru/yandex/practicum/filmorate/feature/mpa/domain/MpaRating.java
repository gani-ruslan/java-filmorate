package ru.yandex.practicum.filmorate.feature.mpa.domain;

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
    private Integer id;
    private String name;

    public MpaRating(MpaRating other) {
        this.id = other.getId();
        this.name = other.getName();
    }
}
