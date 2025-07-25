package ru.yandex.practicum.filmorate.storage.film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

/**
 * Service film.
 */
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final AtomicInteger filmId = new AtomicInteger(0);
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Optional<Film> getFilmById(Long id) {
        if (id == null || !films.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(new Film(films.get(id)));
    }

    @Override
    public List<Film> findAll() {
        return films.values().stream()
                .map(Film::new)
                .toList();
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(getNextId());
        Film createdFilm = new Film(film);
        films.put(film.getId(), createdFilm);
        return new Film(createdFilm);
    }

    @Override
    public void removeFilm(Film film) {
        if (film == null || !films.containsKey(film.getId())) {
            throw new NotFoundException("Film to remove not found with ID: "
                    + (film == null ? null : film.getId()));
        }
        films.remove(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            Film updatedFilm = new Film(film);
            films.put(film.getId(), updatedFilm);
            return new Film(updatedFilm);
        }
        throw new NotFoundException("Film with ID: " + film.getId() + " not found.");
    }

    private Long getNextId() {
        return (long) filmId.incrementAndGet();
    }
}
