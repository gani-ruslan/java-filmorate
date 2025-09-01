package ru.yandex.practicum.filmorate.feature.film.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.common.exception.NotFoundException;
import ru.yandex.practicum.filmorate.feature.film.domain.Film;
import ru.yandex.practicum.filmorate.feature.genre.domain.Genre;
import ru.yandex.practicum.filmorate.feature.user.domain.User;
import ru.yandex.practicum.filmorate.feature.film.repository.FilmRepository;
import ru.yandex.practicum.filmorate.feature.genre.service.GenreService;
import ru.yandex.practicum.filmorate.feature.mpa.service.MpaRatingService;
import ru.yandex.practicum.filmorate.feature.user.service.UserService;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmRepository filmsRepository;
    private final GenreService genreService;
    private final MpaRatingService ratingService;
    private final UserService usersService;

    public FilmService(FilmRepository filmsRepository,
                       GenreService genreService,
                       MpaRatingService ratingService,
                       UserService usersService) {
        this.filmsRepository = filmsRepository;
        this.genreService = genreService;
        this.ratingService = ratingService;
        this.usersService = usersService;
    }

    public List<Film> findAll() {
        return filmsRepository.findAll().stream()
                .map(this::attachAndNormalizeGenres)
                .toList();
    }

    public Film findById(long id) {
        Film film = filmsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Film not found: " + id));
        return attachAndNormalizeGenres(film);
    }

    @Transactional
    public Film create(@Valid Film film) {
        assertMpaExists(film);
        assertGenresExist(film);

        Film created = filmsRepository.create(film);
        updateGenres(created.getId(), film.getGenres());
        return findById(created.getId());
    }

    @Transactional
    public Film update(@Valid Film film) {
        assertMpaExists(film);
        assertGenresExist(film);

        Film updated = filmsRepository.update(film);
        updateGenres(updated.getId(), film.getGenres());
        return findById(updated.getId());
    }

    public Film like(long filmId, long userId) {
        Film film = findById(filmId);
        User user = usersService.findById(userId);
        filmsRepository.like(film.getId(), user.getId());
        return findById(film.getId());
    }

    public Film unlike(long filmId, long userId) {
        Film film = findById(filmId);
        User user = usersService.findById(userId);
        filmsRepository.unlike(film.getId(), user.getId());
        return findById(film.getId());
    }

    public List<Film> findPopular(int count) {
        return filmsRepository.findPopular(count).stream()
                .map(this::attachAndNormalizeGenres)
                .toList();
    }

    /**
     * Helpers.
     */
    private void assertMpaExists(Film film) {
        if (film.getMpa() != null) {
            ratingService.findById(film.getMpa().getId());
        }
    }

    private void assertGenresExist(Film film) {
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genreService.findById(genre.getId());
            }
        }
    }

    private void updateGenres(Long filmId, List<Genre> genres) {
        List<Integer> ids = (genres == null)
                ? List.of()
                : genres.stream()
                    .map(Genre::getId)
                    .toList();
        genreService.updateFilmGenres(filmId, ids);
    }

    private Film attachAndNormalizeGenres(Film film) {
        List<Genre> genres = genreService.getFilmGenresById(film.getId());
        film.setGenres(normalize(genres));
        return film;
    }

    private List<Genre> normalize(List<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            return List.of();
        }

        LinkedHashMap<Integer, Genre> unique = genres.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        Genre::getId,
                        g -> g,
                        (a, b) -> a,
                        LinkedHashMap::new)
                );

        return unique.values().stream()
                .sorted(Comparator.comparingInt(Genre::getId))
                .toList();
    }
}
