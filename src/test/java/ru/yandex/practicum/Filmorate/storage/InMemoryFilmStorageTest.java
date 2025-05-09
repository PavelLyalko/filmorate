package ru.yandex.practicum.Filmorate.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.Filmorate.FilmorateTests;
import ru.yandex.practicum.Filmorate.exception.NotFoundException;
import ru.yandex.practicum.Filmorate.exception.ValidationException;
import ru.yandex.practicum.Filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryFilmStorageTest extends FilmorateTests {

    @Test
    @DisplayName("Проверка коректного создания фильма.")
    void createFilmWithValidData() {
        Film film = createFilm();
        assertDoesNotThrow(() -> filmStorage.create(film));
    }

    @Test
    @DisplayName("Проверка создания фильма если дата релиза невалидна.")
    void createFilmWithInvalidReleaseDate() {
        Film film = createFilm();
        film.setReleaseDate(LocalDate.of(1895, 1, 1));
        ValidationException exception = assertThrows(ValidationException.class, () -> filmStorage.create(film));
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка создания фильма если продолжительность отрицательная.")
    void createFilmWithNegativeDuration() {
        Film film = createFilm();
        film.setDuration(Duration.ofMinutes(-120));
        ValidationException exception = assertThrows(ValidationException.class, () -> filmStorage.create(film));
        assertEquals("Продолжительность фильма должна быть положительным числом", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка коректного обновления фильма если данные валидны.")
    void updateFilmWithValidData() {
        Film film = createFilm();
        filmStorage.create(film);
        Film updatedFilm = new Film();
        updatedFilm.setId(1L);
        updatedFilm.setName("Updated Film");
        updatedFilm.setDescription("Updated Description");
        updatedFilm.setReleaseDate(LocalDate.of(2001, 1, 1));
        updatedFilm.setDuration(Duration.ofMinutes(130));

        assertDoesNotThrow(() -> filmStorage.update(updatedFilm));
        assertEquals("Updated Film", filmStorage.getFilm("1").getName());
    }

    @Test
    @DisplayName("Проверка получения всех фильмов.")
    void getAllFilms() {
        Film film = createFilm();
        filmStorage.create(film);
        Collection<Film> films = filmStorage.getFilms();
        assertFalse(films.isEmpty());
    }

    @Test
    @DisplayName("Проверка получения существующего фильма по Id.")
    void getFilmById() {
        Film film = createFilm();
        filmStorage.create(film);
        Film retrievedFilm = filmStorage.getFilm("1");
        assertNotNull(retrievedFilm);
        assertEquals("testName", retrievedFilm.getName());
    }

    @Test
    @DisplayName("Проверка получения не существующего фильма по Id.")
    void getFilmByNonExistentId() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmStorage.getFilm("50"));
        assertEquals("Фильм с id 50 не найден.", exception.getMessage());
    }
}
