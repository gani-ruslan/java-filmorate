package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.MpaRatingRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilmService {
    private final FilmRepository films;
    private final GenreRepository genres;
    private final MpaRatingRepository ratings;

    public FilmService(FilmRepository films, GenreRepository genres, MpaRatingRepository ratings) {
        this.films = films;
        this.genres = genres;
        this.ratings = ratings;
    }

    @Transactional
    public Film create(@Valid Film film) {
        isMpaExist(film);
        isGenreExist(film);
        Film saved = films.save(film);
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            List<Integer> ids = film.getGenres().stream()
                    .map(Genre::getId)
                    .toList();
            genres.setFilmGenres(saved.getId(), ids);
        } else {
            genres.setFilmGenres(saved.getId(), List.of());
        }

        Film withGenres = films.findById(saved.getId()).orElseThrow();
        withGenres.setGenres(genres.findByFilmId(withGenres.getId()));
        normalizeGenres(withGenres);
        return withGenres;
    }

    @Transactional(readOnly = true)
    public Film findById(long id) {
        Film film = films.findById(id)
                .orElseThrow(() -> new NotFoundException("Film not found: " + id));
        film.setGenres(genres.findByFilmId(id));
        normalizeGenres(film);
        return film;
    }

    public void like(long filmId, long userId) {
        films.like(filmId, userId);
    }

    public void unlike(long filmId, long userId) {
        films.unlike(filmId, userId);
    }

    public List<Film> popular(int limit) {
        return films.findPopular(limit);
    }

    private void isMpaExist(Film film) {
        if (film.getMpaRating() != null) {
            int mpaId = film.getMpaRating().getId();
            ratings.findById(mpaId)
                    .orElseThrow(() -> new NotFoundException("MPA rating not found: " + mpaId));
        }
    }

    private void isGenreExist(Film film) {
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genres.findById(genre.getId())
                        .orElseThrow(() -> new NotFoundException("Genre not found: " + genre.getId()));
            }
        }
    }

    private void normalizeGenres(Film film) {
        List<Genre> list = film.getGenres();
        if (list == null || list.isEmpty()) {
            film.setGenres(List.of());
            return;
        }
        Map<Integer, Genre> unique = new LinkedHashMap<>();
        for (Genre genre : list) {
            if (genre != null) {
                unique.putIfAbsent(genre.getId(), genre);
            }
        }
        List<Genre> normalized = new ArrayList<>(unique.values());
        normalized.sort(Comparator.comparingInt(Genre::getId));
        film.setGenres(normalized);
    }
}
