package ru.yandex.practicum.Filmorate.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.Filmorate.FilmorateTests;
import ru.yandex.practicum.Filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThat;

class InMemoryFilmStorageTest extends FilmorateTests {

    @Test
    @DisplayName("Проверка коректного создания фильма.")
    void createFilmWithValidData() {
        Film film = createFilm();

        assertThatNoException().isThrownBy(() -> filmStorage.create(film));
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

        assertThatNoException().isThrownBy(() -> filmStorage.update(updatedFilm));
        assertThat(filmStorage.getFilm(1L).get().getName()).isEqualTo("Updated Film");
    }

    @Test
    @DisplayName("Проверка получения всех фильмов.")
    void getAllFilms() {
        Film film = createFilm();
        filmStorage.create(film);
        Collection<Film> films = filmStorage.getFilms();

        assertThat(films).isNotEmpty();
    }

    @Test
    @DisplayName("Проверка получения существующего фильма по Id.")
    void getFilmById() {
        Film film = createFilm();
        filmStorage.create(film);
        Film retrievedFilm = filmStorage.getFilm(1L).get();

        assertThat(retrievedFilm).isNotNull();
        assertThat(retrievedFilm.getName()).isEqualTo("testName");
    }

    @Test
    @DisplayName("Проверка добавление лайка.")
    void putLike() {
        Film film = createFilm();
        filmStorage.create(film);
        filmStorage.getFilm(film.getId()).get().putLike(100L);

        assertThat(film.getFilmLikes()).contains(100L);
    }

    @Test
    @DisplayName("Проверка удаления лайка.")
    void deleteLike() {
        Film film = createFilm();
        filmStorage.create(film);
        film.getFilmLikes().add(100L);
        filmStorage.getFilm(film.getId()).get().deleteLike(100L);

        assertThat(film.getFilmLikes()).doesNotContain(100L);
    }
}
