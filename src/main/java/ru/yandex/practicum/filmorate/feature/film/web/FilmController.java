package ru.yandex.practicum.filmorate.feature.film.web;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.feature.film.dto.CreateFilmRequest;
import ru.yandex.practicum.filmorate.feature.film.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.feature.film.dto.FilmDto;
import ru.yandex.practicum.filmorate.feature.film.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.feature.film.service.FilmService;

@RestController
@RequestMapping("/films")
@Validated
public class FilmController {

    private final FilmService filmService;
    private final FilmMapper mapper;

    public FilmController(FilmService filmService,
                          FilmMapper mapper) {
        this.filmService = filmService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<FilmDto> findAll() {
        return mapper.toDto(filmService.findAll());
    }

    @GetMapping("/{id}")
    public FilmDto findById(@PathVariable long id) {
        return mapper.toDto(filmService.findById(id));
    }

    @GetMapping("/popular")
    public List<FilmDto> findPopular(@RequestParam(defaultValue = "10") @Min(1) int count) {
        return mapper.toDto(filmService.findPopular(count));
    }

    @PostMapping
    public FilmDto create(@Valid @RequestBody CreateFilmRequest dto) {
        return mapper.toDto(filmService.create(mapper.toDomainCreate(dto)));
    }

    @PutMapping
    public FilmDto update(@Valid @RequestBody UpdateFilmRequest dto) {
        return mapper.toDto(filmService.update(mapper.toDomainUpdate(dto)));
    }

    @PutMapping("/{filmId}/like/{userId}")
    public FilmDto addLike(@PathVariable long filmId, @PathVariable long userId) {
        return mapper.toDto(filmService.like(filmId, userId));
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public FilmDto removeLike(@PathVariable long filmId, @PathVariable long userId) {
        return mapper.toDto(filmService.unlike(filmId, userId));
    }
}
