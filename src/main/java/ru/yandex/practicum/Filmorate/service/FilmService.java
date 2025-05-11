package ru.yandex.practicum.Filmorate.service;

import ru.yandex.practicum.Filmorate.model.Film;

import java.util.List;

public interface FilmService {
    List<Film> getPopularFilms(String count);
}
