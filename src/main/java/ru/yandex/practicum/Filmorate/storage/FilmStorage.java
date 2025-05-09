package ru.yandex.practicum.Filmorate.storage;

import jakarta.validation.Valid;
import ru.yandex.practicum.Filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    void create(Film film);

    void update(@Valid Film updateFilm);

    Collection<Film> getFilms();

    Film getFilm(String id);
}

