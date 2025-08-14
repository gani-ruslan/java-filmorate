package ru.yandex.practicum.filmorate.web.controller;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.web.dto.FilmDto;
import ru.yandex.practicum.filmorate.web.mapper.FilmDtoMapper;

@RestController
@RequestMapping("/films")
@Validated
public class FilmController {

    private final FilmService filmService;
    private final FilmRepository films;
    private final GenreRepository genres;
    private final FilmDtoMapper mapper;

    public FilmController(FilmService filmService,
                          FilmRepository films,
                          GenreRepository genres,
                          FilmDtoMapper mapper) {
        this.filmService = filmService;
        this.films = films;
        this.genres = genres;
        this.mapper = mapper;
    }

    @GetMapping
    public List<FilmDto> findAll() {
        return films.findAll().stream()
                .peek(film -> film.setGenres(genres.findByFilmId(film.getId())))
                .map(mapper::toDto) // mapper сам нормализует жанры
                .toList();
    }

    @GetMapping("/{id}")
    public FilmDto findById(@PathVariable long id) {
        Film film = filmService.findById(id); // подтягивает и нормализует жанры
        return mapper.toDto(film);
    }

    @GetMapping("/popular")
    public List<FilmDto> getPopular(@RequestParam(defaultValue = "10") @Min(1) int count) {
        return films.findPopular(count).stream()
                .peek(film -> film.setGenres(genres.findByFilmId(film.getId())))
                .map(mapper::toDto)
                .toList();
    }

    @PostMapping
    public FilmDto create(@Valid @RequestBody FilmDto dto) {
        Film saved = filmService.create(mapper.toDomain(dto));
        saved.setGenres(genres.findByFilmId(saved.getId()));
        return mapper.toDto(saved);
    }

    @PutMapping
    public FilmDto update(@Valid @RequestBody FilmDto dto) {
        Film domain = mapper.toDomain(dto);
        Film saved = films.save(domain);
        if (dto.getGenres() != null) {
            List<Integer> ids = dto.getGenres().stream().map(Genre::getId).toList();
            genres.setFilmGenres(saved.getId(), ids);
        }
        saved.setGenres(genres.findByFilmId(saved.getId()));
        return mapper.toDto(saved);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public FilmDto addLike(@PathVariable long filmId, @PathVariable long userId) {
        films.like(filmId, userId);
        Film film = filmService.findById(filmId); // единая точка нормализации
        return mapper.toDto(film);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public FilmDto removeLike(@PathVariable long filmId, @PathVariable long userId) {
        films.unlike(filmId, userId);
        Film film = filmService.findById(filmId);
        return mapper.toDto(film);
    }
}
