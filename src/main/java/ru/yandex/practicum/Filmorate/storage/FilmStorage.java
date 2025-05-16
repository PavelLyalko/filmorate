package ru.yandex.practicum.Filmorate.storage;

import ru.yandex.practicum.Filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    void create(Film film);

    void update(Film updateFilm);

    Collection<Film> getFilms();

    Optional<Film> getFilm(String id);
}

