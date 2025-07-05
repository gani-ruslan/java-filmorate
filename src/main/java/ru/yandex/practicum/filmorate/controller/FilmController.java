package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final AtomicInteger filmId = new AtomicInteger(0);
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values().stream()
                .map(Film::new)
                .toList();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        film.setId(getNextId());
        Film createdFilm = new Film(film);
        films.put(film.getId(), createdFilm);
        return new Film(createdFilm);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            Film updatedFilm = new Film(film);
            films.put(film.getId(), updatedFilm);
            return new Film(updatedFilm);
        }
        throw new NotFoundException("Film with ID: " + film.getId() + " not found.");
    }

    private int getNextId() {
        return filmId.incrementAndGet();
    }
}