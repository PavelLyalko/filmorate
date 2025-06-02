package ru.yandex.practicum.Filmorate.service;

import ru.yandex.practicum.Filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    List<Genre> getAll();

    Optional<Genre> getById(int id);

    boolean existsById(int id);
}
