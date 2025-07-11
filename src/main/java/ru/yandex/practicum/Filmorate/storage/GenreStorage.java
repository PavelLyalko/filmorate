package ru.yandex.practicum.Filmorate.storage;

import ru.yandex.practicum.Filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreStorage {
    List<Genre> getAll();

    Optional<Genre> getById(Integer id);

    boolean existsById(Integer id);

    Set<Genre> resolveGenres(Set<Genre> genres);
}
