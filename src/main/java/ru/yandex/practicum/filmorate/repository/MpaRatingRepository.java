package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.MpaRating;
import java.util.List;
import java.util.Optional;

public interface MpaRatingRepository {
    List<MpaRating> findAll();

    Optional<MpaRating> findById(int id);
}
