package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    Film save(Film film);                 // create/update по id

    Optional<Film> findById(long id);

    List<Film> findAll();

    boolean deleteById(long id);

    void like(long filmId, long userId);

    void unlike(long filmId, long userId);

    List<Film> findPopular(int limit);
}
