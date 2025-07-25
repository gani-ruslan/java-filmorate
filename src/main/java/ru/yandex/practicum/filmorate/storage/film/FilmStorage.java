package ru.yandex.practicum.filmorate.storage.film;

import java.util.List;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {

    Optional<Film> getFilmById(Long id);

    Film addFilm(Film film);

    void removeFilm(Film film);

    Film updateFilm(Film film);

    List<Film> findAll();
}
