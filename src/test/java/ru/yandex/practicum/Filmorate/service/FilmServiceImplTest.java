package ru.yandex.practicum.Filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.Filmorate.FilmorateTests;
import ru.yandex.practicum.Filmorate.exception.NotFoundException;
import ru.yandex.practicum.Filmorate.model.Film;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class FilmServiceImplTest extends FilmorateTests {
    @BeforeEach
    void setUp() {
        filmStorage.getFilms().clear();
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

        List<Film> popularFilms = filmService.getPopularFilms(10);

        assertThat(2).isEqualTo(popularFilms.size());
        assertThat(2).isEqualTo(popularFilms.get(0).getFilmLikes().size());
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

        List<Film> popularFilms = filmService.getPopularFilms(1);

        assertThat(1).isEqualTo(popularFilms.size());
        assertThat(2).isEqualTo(popularFilms.get(0).getFilmLikes().size());
    }

    @Test
    @DisplayName("Проверка получения не существующего фильма по Id.")
    void getFilmByNonExistentId() {
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> filmService.getFilm(50L))
                .withMessage("Фильм с id 50 не найден.");
    }
}
