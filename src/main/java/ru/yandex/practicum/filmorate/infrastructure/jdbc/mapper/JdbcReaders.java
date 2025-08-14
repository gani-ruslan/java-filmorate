package ru.yandex.practicum.filmorate.infrastructure.jdbc.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class JdbcReaders {
    private JdbcReaders() {
    }

    public static LocalDate getLocalDate(ResultSet resultSet, String col)
            throws SQLException {
        Date date = resultSet.getDate(col);
        return date != null ? date.toLocalDate() : null;
    }
}
