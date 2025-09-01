package ru.yandex.practicum.filmorate.infrastructure.jdbc.core;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.common.repository.BaseRepository;

public abstract class BaseJdbcRepository<T, ID> implements BaseRepository<T, ID> {

    protected final NamedParameterJdbcTemplate jdbc;
    protected final RowMapper<T> rowMapper;

    protected BaseJdbcRepository (NamedParameterJdbcTemplate jdbc,
                                  RowMapper<T> rowMapper) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
    }

    protected List<T> queryMany(String sqlQuery) {
        return jdbc.query(sqlQuery, rowMapper);
    }

    protected List<T> queryMany(String sqlQuery, Map<String, ?> params) {
        return jdbc.query(sqlQuery, params, rowMapper);
    }

    protected Optional<T> queryOne(String sqlQuery, Map<String, ?> params) {
        List<T> list = jdbc.query(sqlQuery, params, rowMapper);
        return list.stream().findFirst();
    }

    protected Optional<ID> createEntry(String sqlQuery, MapSqlParameterSource params) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(
                sqlQuery,
                params,
                keyHolder,
                new String[]{"id"}
        );
        return keyHolder.getKey() != null
                ? Optional.of(convert(keyHolder.getKey()))
                : Optional.empty();
    }

    protected int execUpdate(String sqlQuery, MapSqlParameterSource params) {
        return jdbc.update(sqlQuery, params);
    }

    protected int[] execUpdate(String sqlQuery, MapSqlParameterSource[] params) {
        return jdbc.batchUpdate(sqlQuery, params);
    }

    protected int execUpdate(String sqlQuery, Map<String, ?> params) {
        return jdbc.update(sqlQuery, params);
    }

    protected abstract ID convert(Number data);
}
