package ru.yandex.practicum.filmorate.infrastructure.memory;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Profile("inmemory")
public class GenreInMemoryRepository implements GenreRepository {

    private final Map<Integer, Genre> genres = new ConcurrentHashMap<>();
    private final Map<Long, Set<Integer>> filmGenres = new ConcurrentHashMap<>();

    public GenreInMemoryRepository() {
        genres.put(1, new Genre(1, "Comedy"));
        genres.put(2, new Genre(2, "Drama"));
        genres.put(3, new Genre(3, "Action"));
    }

    @Override
    public List<Genre> findAll() {
        List<Genre> list = new ArrayList<>(genres.values());
        list.sort(Comparator.comparingInt(Genre::getId));
        return list;
    }

    @Override
    public Optional<Genre> findById(int id) {
        Genre genre = genres.get(id);
        return Optional.ofNullable(genre == null ? null : new Genre(genre));
    }

    @Override
    public List<Genre> findByFilmId(long filmId) {
        Set<Integer> ids = filmGenres.getOrDefault(filmId, Collections.emptySet());
        List<Genre> list = new ArrayList<>();
        for (Integer id : ids) {
            Genre genre = genres.get(id);
            if (genre != null) list.add(new Genre(genre));
        }
        list.sort(Comparator.comparingInt(Genre::getId));
        return list;
    }

    @Override
    public void setFilmGenres(long filmId, Collection<Integer> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) {
            filmGenres.remove(filmId);
            return;
        }
        Set<Integer> set = new TreeSet<>();
        for (Integer id : genreIds) {
            if (id != null && genres.containsKey(id)) set.add(id);
        }
        filmGenres.put(filmId, set);
    }
}
