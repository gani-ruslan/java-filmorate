package ru.yandex.practicum.filmorate.infrastructure.jdbc.mpa.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.feature.mpa.domain.MpaRating;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.core.BaseJdbcRepository;
import ru.yandex.practicum.filmorate.infrastructure.jdbc.mpa.mapper.MpaRatingRowMapper;
import ru.yandex.practicum.filmorate.feature.mpa.repository.MpaRatingRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class MpaRatingJdbcRepository extends BaseJdbcRepository<MpaRating, Integer> implements MpaRatingRepository {

    public MpaRatingJdbcRepository(NamedParameterJdbcTemplate jdbc,
                               MpaRatingRowMapper mpaRatingRowMapper) {
        super(jdbc, mpaRatingRowMapper);
    }

    private static final class Sql {
        private static final String SELECT_ALL = "SELECT id, name FROM mpa_rating ORDER BY id";

        private static final String SELECT_BY_ID = "SELECT id, name FROM mpa_rating WHERE id=:id";
    }

    /* Default repository implementation */
    @Override
    public List<MpaRating> findAll() {
        return queryMany(Sql.SELECT_ALL);
    }

    @Override
    public Optional<MpaRating> findById(Integer id) {
        return queryOne(Sql.SELECT_BY_ID, Map.of("id", id));
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

    @Override
    protected Integer convert(Number data) {
        return data.intValue();
    }
}
