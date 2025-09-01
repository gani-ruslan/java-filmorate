package ru.yandex.practicum.filmorate.infrastructure.memory.mpa.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.feature.mpa.domain.MpaRating;
import ru.yandex.practicum.filmorate.feature.mpa.repository.MpaRatingRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Profile("inmemory")
public class MpaRatingInMemoryRepository implements MpaRatingRepository {

    private final Map<Integer, MpaRating> ratings = new ConcurrentHashMap<>();

    public MpaRatingInMemoryRepository() {
        ratings.put(1, new MpaRating(1, "G"));
        ratings.put(2, new MpaRating(2, "PG"));
        ratings.put(3, new MpaRating(3, "PG-13"));
        ratings.put(4, new MpaRating(4, "R"));
    }

    @Override
    public List<MpaRating> findAll() {
        List<MpaRating> list = new ArrayList<>(ratings.values());
        list.sort(Comparator.comparingInt(MpaRating::getId));
        return list;
    }

    @Override
    public Optional<MpaRating> findById(Integer id) {
        MpaRating mpaRating = ratings.get(id);
        return Optional.ofNullable(mpaRating == null ? null : new MpaRating(mpaRating));
    }

    @Override
    public MpaRating create(MpaRating mpaRating) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MpaRating update(MpaRating mpaRating) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean deleteById(Integer id) {
        throw new UnsupportedOperationException();
    }
}
