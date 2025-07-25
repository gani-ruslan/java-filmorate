package ru.yandex.practicum.filmorate.service.film;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.config.AppMode;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Service
public class FilmService {

    private final AppMode config;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(AppMode config, FilmStorage filmStorage, UserStorage userStorage) {
        this.config = config;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film addLike(Long filmId, Long userId) {
        Film film = getFilmOrThrow(filmId);
        User user = getUserOrThrow(userId);

        film.likeFilm(user.getId());
        filmStorage.updateFilm(film);

        return new Film(film);
    }

    public Film removeLike(Long filmId, Long userId) {
        Film film = getFilmOrThrow(filmId);
        User user = getUserOrThrow(userId);

        film.unlikeFilm(user.getId());
        filmStorage.updateFilm(film);

        return new Film(film);
    }

    public List<Film> getPopularFilms() {
        return getPopularFilms(config.getMaxPopularCount());
    }

    public List<Film> getPopularFilms(int limit) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(limit)
                .map(Film::new)
                .collect(Collectors.toList());
    }


    private Film getFilmOrThrow(Long id) {
        return filmStorage.getFilmById(id)
                .orElseThrow(() -> new NotFoundException("Film not found with ID: " + id));
    }

    private User getUserOrThrow(Long id) {
        return userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
    }
}