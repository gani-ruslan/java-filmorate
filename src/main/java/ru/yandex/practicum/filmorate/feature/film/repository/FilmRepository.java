package ru.yandex.practicum.filmorate.feature.film.repository;

import ru.yandex.practicum.filmorate.common.repository.BaseRepository;
import ru.yandex.practicum.filmorate.feature.film.domain.Film;
import java.util.List;

public interface FilmRepository extends BaseRepository<Film, Long> {
    void like(Long filmId, Long userId);

    void unlike(Long filmId, Long userId);

    List<Film> findPopular(int count);
}
