package ru.yandex.practicum.filmorate.infrastructure.memory.film.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.feature.film.domain.Film;
import ru.yandex.practicum.filmorate.feature.film.repository.FilmRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("inmemory")
public class FilmInMemoryRepository implements FilmRepository {

    private final ConcurrentHashMap<Long, Film> films = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    @Override
    public List<Film> findAll() {
        List<Film> list = new ArrayList<>();
        for (Film film : films.values()) {
            list.add(new Film(film));
        }
        return list;
    }

    @Override
    public Optional<Film> findById(Long id) {
        Film stored = films.get(id);
        return stored == null ? Optional.empty() : Optional.of(new Film(stored));
    }

    @Override
    public Film create(Film film) {
        if (film.getId() == null || film.getId() == 0L) {
            long id = seq.incrementAndGet();
            film.setId(id);
        }
        films.put(film.getId(), new Film(film));
        return new Film(films.get(film.getId()));
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), new Film(film));
        return new Film(films.get(film.getId()));
    }

    @Override
    public boolean deleteById(Long id) {
        return films.remove(id) != null;
    }

    @Override
    public void like(Long filmId, Long userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unlike(Long filmId, Long userId) {
        throw new UnsupportedOperationException();

    }

    @Override
    public List<Film> findPopular(int limit) {
        throw new UnsupportedOperationException();
    }
}
