package ru.yandex.practicum.filmorate.feature.genre.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Genre.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    private Integer id;
    private String name;

    public Genre(Genre genre) {
        this.id = genre.getId();
        this.name = genre.getName();
    }
}
