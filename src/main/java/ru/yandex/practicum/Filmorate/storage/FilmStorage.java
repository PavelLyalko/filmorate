package ru.yandex.practicum.Filmorate.storage;

import ru.yandex.practicum.Filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Film create(Film film);

    Optional<Film> update(Film updateFilm);

    Collection<Film> getFilms();

    Optional<Film> getFilmById(Long id);
}

