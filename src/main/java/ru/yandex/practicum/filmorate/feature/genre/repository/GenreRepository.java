package ru.yandex.practicum.filmorate.feature.genre.repository;

import ru.yandex.practicum.filmorate.common.repository.BaseRepository;
import ru.yandex.practicum.filmorate.feature.genre.domain.Genre;
import java.util.Collection;
import java.util.List;

public interface GenreRepository extends BaseRepository<Genre, Integer> {

    List<Genre> getFilmGenres(Long filmId);

    void setFilmGenres(Long filmId, Collection<Integer> genreIds);
}
