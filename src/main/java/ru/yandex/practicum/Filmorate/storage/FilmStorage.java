package ru.yandex.practicum.Filmorate.storage;

import ru.yandex.practicum.Filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    void create(Film film);

    void update(Film updateFilm);

    Collection<Film> getFilms();

    Film getFilm(String id);
}

