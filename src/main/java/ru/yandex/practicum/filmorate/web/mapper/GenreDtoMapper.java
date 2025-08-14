package ru.yandex.practicum.filmorate.web.mapper;

import ru.yandex.practicum.filmorate.web.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

public final class GenreDtoMapper {
    private GenreDtoMapper() {
    }

    public static GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}