package ru.yandex.practicum.Filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.Filmorate.FilmorateTests;
import ru.yandex.practicum.Filmorate.model.Film;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmServiceImplTest extends FilmorateTests {
    @BeforeEach
    void setUp() {
        filmStorage.getFilms().clear();
    }

    @Test
    @DisplayName("Проверка добавление лайка.")
    void putLike() {
        Film film = createFilm();
        filmStorage.create(film);
        filmService.putLike(1L, 100L);
        assertTrue(film.getFilmLikes().contains(100L));
    }

    @Test
    @DisplayName("Проверка удаления лайка.")
    void deleteLike() {
        Film film = createFilm();
        filmStorage.create(film);
        film.getFilmLikes().add(100L);
        filmService.deleteLike(1L, 100L);
        assertFalse(film.getFilmLikes().contains(100L));
    }

    @Test
    @DisplayName("Проверка получение списка популярных фильмов со значением по умолчанию.")
    void getPopularFilmsWithDefaultCount() {
        Film film1 = createFilm();
        film1.setFilmLikes(new HashSet<>(Arrays.asList(1L, 2L)));

        Film film2 = createFilm();
        film2.setId(2L);
        film2.setFilmLikes(new HashSet<>(Arrays.asList(1L, 2L)));

        filmStorage.create(film1);
        filmStorage.create(film2);

        List<Film> popularFilms = filmService.getPopularFilms("10");
        System.out.println(popularFilms);
        assertEquals(2, popularFilms.size());
        assertEquals(2, popularFilms.get(0).getFilmLikes().size());
    }

    @Test
    @DisplayName("Проверка получение списка популярных фильмов определенного размера.")
    void getPopularFilmsWithSpecificCount() {
        Film film1 = createFilm();
        film1.setFilmLikes(new HashSet<>(Arrays.asList(1L, 2L)));

        Film film2 = createFilm();
        film2.setId(2L);
        film2.setFilmLikes(new HashSet<>(Arrays.asList(1L, 2L)));

        filmStorage.create(film1);
        filmStorage.create(film2);

        List<Film> popularFilms = filmService.getPopularFilms("1");
        assertEquals(1, popularFilms.size());
        assertEquals(2, popularFilms.get(0).getFilmLikes().size());
    }
}
