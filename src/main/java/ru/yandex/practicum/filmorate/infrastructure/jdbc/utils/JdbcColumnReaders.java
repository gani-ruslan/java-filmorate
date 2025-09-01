package ru.yandex.practicum.filmorate.infrastructure.jdbc.utils;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class JdbcColumnReaders {
    private JdbcColumnReaders() {
    }

    public static LocalDate getLocalDate(ResultSet resultSet, String col)
            throws SQLException {
        Date date = resultSet.getDate(col);
        return date != null ? date.toLocalDate() : null;
    }
}
