package ru.yandex.practicum.filmorate.web.mapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.web.dto.FilmDto;

@Component
public class FilmDtoMapper {

    public Film toDomain(FilmDto dto) {
        Film film = new Film();
        film.setId(dto.getId());
        film.setName(dto.getName());
        film.setDescription(dto.getDescription());
        film.setReleaseDate(dto.getReleaseDate());
        film.setDuration(dto.getDuration());
        film.setLikes(dto.getLikes() == null ? new java.util.HashSet<>() : dto.getLikes());
        film.setMpaRating(dto.getMpa() != null
                ? new MpaRating(dto.getMpa().getId(), dto.getMpa().getName())
                : null
        );
        film.setGenres(dto.getGenres() != null ? dto.getGenres() : List.of());
        return film;
    }

    public FilmDto toDto(Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());
        dto.setLikes(film.getLikes());
        dto.setMpa(film.getMpaRating());

        List<Genre> genres = film.getGenres();
        if (genres == null || genres.isEmpty()) {
            dto.setGenres(new ArrayList<>());
        } else {
            dto.setGenres(normalizeGenres(genres));
        }

        return dto;
    }

    private List<Genre> normalizeGenres(List<Genre> list) {
        Map<Integer, Genre> unique = new LinkedHashMap<>();
        for (Genre genre : list) {
            if (genre != null) {
                unique.putIfAbsent(genre.getId(), genre);
            }
        }
        List<Genre> normalized = new ArrayList<>(unique.values());
        normalized.sort(Comparator.comparingInt(Genre::getId));
        return normalized;
    }
}
