package ru.yandex.practicum.filmorate.infrastructure.jdbc.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.mapper.MpaRatingRowMapper;
import ru.yandex.practicum.filmorate.repository.MpaRatingRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class MpaRatingJdbcRepository implements MpaRatingRepository {

    private final NamedParameterJdbcOperations jdbc;
    private final MpaRatingRowMapper mpaMapper;

    public MpaRatingJdbcRepository(NamedParameterJdbcOperations jdbc,
                                   MpaRatingRowMapper mpaMapper) {
        this.jdbc = jdbc;
        this.mpaMapper = mpaMapper;
    }

    private static final class Sql {
        private static final String SELECT_ALL = "SELECT id, name FROM mpa_rating ORDER BY id";

        private static final String SELECT_BY_ID = "SELECT id, name FROM mpa_rating WHERE id=:id";
    }

    @Override
    public List<MpaRating> findAll() {
        return jdbc.query(Sql.SELECT_ALL, mpaMapper);
    }

    @Override
    public Optional<MpaRating> findById(int id) {
        List<MpaRating> list = jdbc.query(Sql.SELECT_BY_ID, Map.of("id", id), mpaMapper);
        return list.stream().findFirst();
    }
}
