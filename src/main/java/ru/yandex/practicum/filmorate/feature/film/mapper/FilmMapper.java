package ru.yandex.practicum.filmorate.feature.film.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.feature.film.domain.Film;
import ru.yandex.practicum.filmorate.feature.film.dto.FilmDto;
import ru.yandex.practicum.filmorate.feature.film.dto.CreateFilmRequest;
import ru.yandex.practicum.filmorate.feature.film.dto.FilmPayload;
import ru.yandex.practicum.filmorate.feature.film.dto.UpdateFilmRequest;


@Component
public class FilmMapper {

    /**
     * In.
     */
    public Film toDomainCreate(CreateFilmRequest dto) {
        return createCommonFilm(new Film(), dto);
    }

    public Film toDomainUpdate(UpdateFilmRequest dto) {
        Film film = createCommonFilm(new Film(), dto);
        film.setId(dto.getId());
        return film;
    }

    /**
     * Out.
     */
    public FilmDto toDto(Film film) {
        return createDto(film);
    }

    public List<FilmDto> toDto(List<Film> films) {
        return films.stream()
                .map(this::createDto)
                .toList();
    }

    /**
     * Helpers.
     */
    private FilmDto createDto(Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());
        dto.setMpa(film.getMpa());
        dto.setGenres(film.getGenres());
        return dto;
    }

    private <T extends FilmPayload> Film createCommonFilm(Film film, T dto) {
        film.setName(dto.getName());
        film.setDescription(dto.getDescription());
        film.setReleaseDate(dto.getReleaseDate());
        film.setDuration(dto.getDuration());
        film.setMpa(dto.getMpa());
        film.setGenres(dto.getGenres());
        return film;
    }
}

