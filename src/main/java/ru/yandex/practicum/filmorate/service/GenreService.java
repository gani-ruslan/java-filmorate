package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import java.util.List;

@Service
public class GenreService {
    private final GenreRepository genres;

    public GenreService(GenreRepository genres) {
        this.genres = genres;
    }

    public List<Genre> findAll() {
        return genres.findAll();
    }

    public Genre findById(int id) {
        return genres.findById(id)
                .orElseThrow(() -> new NotFoundException("Genre not found: " + id));
    }
}