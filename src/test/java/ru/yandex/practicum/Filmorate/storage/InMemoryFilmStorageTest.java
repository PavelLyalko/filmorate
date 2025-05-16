package ru.yandex.practicum.Filmorate.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.Filmorate.FilmorateTests;
import ru.yandex.practicum.Filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryFilmStorageTest extends FilmorateTests {

    @Test
    @DisplayName("Проверка коректного создания фильма.")
    void createFilmWithValidData() {
        Film film = createFilm();
        assertDoesNotThrow(() -> filmStorage.create(film));
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
        assertEquals("Updated Film", filmStorage.getFilm("1").get().getName());
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
        Film retrievedFilm = filmStorage.getFilm("1").get();
        assertNotNull(retrievedFilm);
        assertEquals("testName", retrievedFilm.getName());
    }

    @Test
    @DisplayName("Проверка добавление лайка.")
    void putLike() {
        Film film = createFilm();
        filmStorage.create(film);
        filmStorage.getFilm(film.getId().toString()).get().putLike(100L);
        assertTrue(film.getFilmLikes().contains(100L));
    }

    @Test
    @DisplayName("Проверка удаления лайка.")
    void deleteLike() {
        Film film = createFilm();
        filmStorage.create(film);
        film.getFilmLikes().add(100L);
        filmStorage.getFilm(film.getId().toString()).get().deleteLike(100L);
        assertFalse(film.getFilmLikes().contains(100L));
    }
}
