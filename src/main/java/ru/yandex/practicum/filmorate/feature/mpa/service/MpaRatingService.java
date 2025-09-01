package ru.yandex.practicum.filmorate.feature.mpa.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.common.exception.NotFoundException;
import ru.yandex.practicum.filmorate.feature.mpa.domain.MpaRating;
import ru.yandex.practicum.filmorate.feature.mpa.repository.MpaRatingRepository;
import java.util.List;

@Service
public class MpaRatingService {
    private final MpaRatingRepository ratingRepository;

    public MpaRatingService(MpaRatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public List<MpaRating> findAll() {
        return ratingRepository.findAll();
    }

    public MpaRating findById(Integer id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("MPA rating not found: " + id));
    }
}
