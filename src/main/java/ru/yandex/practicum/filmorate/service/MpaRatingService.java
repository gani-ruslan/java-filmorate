package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.repository.MpaRatingRepository;
import java.util.List;

@Service
public class MpaRatingService {
    private final MpaRatingRepository ratings;

    public MpaRatingService(MpaRatingRepository ratings) {
        this.ratings = ratings;
    }

    public List<MpaRating> findAll() {
        return ratings.findAll();
    }

    public MpaRating findById(int id) {
        return ratings.findById(id)
                .orElseThrow(() -> new NotFoundException("MPA rating not found: " + id));
    }
}
