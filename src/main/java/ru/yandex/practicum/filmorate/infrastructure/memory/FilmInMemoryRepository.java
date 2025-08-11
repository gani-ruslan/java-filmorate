package ru.yandex.practicum.filmorate.infrastructure.memory;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import java.util.ArrayList;
import java.util.Comparator;
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
    public Film save(Film film) {
        if (film.getId() == null || film.getId() == 0L) {
            long id = seq.incrementAndGet();
            film.setId(id);
        }
        films.put(film.getId(), new Film(film));
        return new Film(films.get(film.getId()));
    }

    @Override
    public Optional<Film> findById(long id) {
        Film stored = films.get(id);
        return stored == null ? Optional.empty() : Optional.of(new Film(stored));
    }

    @Override
    public List<Film> findAll() {
        List<Film> list = new ArrayList<>();
        for (Film film : films.values()) {
            list.add(new Film(film));
        }
        return list;
    }

    @Override
    public boolean deleteById(long id) {
        return films.remove(id) != null;
    }

    @Override
    public void like(long filmId, long userId) {
        Film stored = films.get(filmId);
        if (stored == null) throw new NotFoundException("Film not found: " + filmId);
        stored.getLikes().add(userId);
    }

    @Override
    public void unlike(long filmId, long userId) {
        Film stored = films.get(filmId);
        if (stored == null) throw new NotFoundException("Film not found: " + filmId);
        stored.getLikes().remove(userId);
    }

    @Override
    public List<Film> findPopular(int limit) {
        List<Film> list = findAll(); // уже копии
        list.sort(Comparator.comparingInt(a -> -a.getLikes().size()));
        if (limit < list.size()) {
            return list.subList(0, limit);
        }
        return list;
    }
}
