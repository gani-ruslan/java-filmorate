package ru.yandex.practicum.filmorate.feature.genre.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.feature.genre.dto.GenreDto;
import ru.yandex.practicum.filmorate.feature.genre.domain.Genre;

@Component
public class GenreDtoMapper {
    /**
     * In.
     */
    public Genre toDomain(GenreDto dto) {
        Genre genre = new Genre();
        genre.setId(dto.getId());
        genre.setName(dto.getName());
        return genre;
    }

    /**
     * Out.
     */
    public GenreDto toDto(Genre genre) {
        return createDto(genre);
    }

    public List<GenreDto> toDto (List<Genre> genres) {
        return genres.stream()
                .map(this::createDto)
                .toList();
    }

    /**
     * Helpers.
     */
    public GenreDto createDto(Genre genre) {
        GenreDto dto = new GenreDto();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        return dto;
    }

}