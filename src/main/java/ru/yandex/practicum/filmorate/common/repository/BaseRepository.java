package ru.yandex.practicum.filmorate.common.repository;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T, ID> {
    List<T> findAll();

    Optional<T> findById(ID id);

    T create(T model);

    T update(T model);

    boolean deleteById(ID id);
}
