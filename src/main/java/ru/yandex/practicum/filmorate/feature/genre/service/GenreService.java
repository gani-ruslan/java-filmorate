package ru.yandex.practicum.filmorate.feature.genre.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.common.exception.NotFoundException;
import ru.yandex.practicum.filmorate.feature.genre.domain.Genre;
import ru.yandex.practicum.filmorate.feature.genre.repository.GenreRepository;
import java.util.Collection;
import java.util.List;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    public Genre findById(Integer id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Genre not found: " + id));
    }

    public List<Genre> getFilmGenresById(Long filmId) {
        return genreRepository.getFilmGenres(filmId);
    }

    public void updateFilmGenres(Long filmId,
                                 Collection<Integer> genreIds) {
        genreRepository.setFilmGenres(filmId, genreIds);
    }
}