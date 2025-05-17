package ru.yandex.practicum.Filmorate.contoller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.Filmorate.FilmorateTests;
import ru.yandex.practicum.Filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FilmControllerTest extends FilmorateTests {

    @Test
    @DisplayName("Проверка успешного добавления фильма, если все поля валидны")
    void successCreateUserTest() {
        Film film = createFilm();

        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertThat(response.getBody()).isNotNull();
        assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
        assertThat(response.getBody()).isEqualTo(film);
    }

    @Test
    @DisplayName("Проверка получения всех фильмов")
    void successGetAllFilms() {
        Film film1 = createFilm();
        Film film2 = createFilm();
        film2.setId(2L);

        restTemplate.postForEntity("/films", film1, Film.class);
        restTemplate.postForEntity("/films", film2, Film.class);

        ResponseEntity<Collection> response = restTemplate.getForEntity("/films", Collection.class);

        assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
        assertThat("[{id=1, name=testName, description=тестофый фильм, releaseDate=1991-02-01, duration=PT2H, filmLikes=[]}, {id=2, name=testName, description=тестофый фильм, releaseDate=1991-02-01, duration=PT2H, filmLikes=[]}, {id=3, name=testName, description=тестофый фильм, releaseDate=1991-02-01, duration=PT2H, filmLikes=[]}]").isEqualTo(response.getBody().toString());
    }

    @ValueSource(strings = {"null", ""})
    @ParameterizedTest
    @DisplayName("Проеверяет добавление фильма с пустым названием")
    void badRequestCreateFilmWhenNameInvalidTest(String name) {
        Film film = createFilm();
        String newName = "null".equals(name) ? null : name;
        film.setName(newName);

        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);

        assertThat("{\"name\":\"Название не может быть пустым.\"}").isEqualTo(response.getBody().toString());
        assertThat(HttpStatus.BAD_REQUEST).isEqualTo(response.getStatusCode());
    }

    @Test
    @DisplayName("проверяем добавления, если описание больше 200 символов")
    void badRequestCreateFilmWhenDescriptionInvalidTest() {
        Film film = createFilm();
        film.setDescription("fdfdsdsfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffdfdsdsfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffdfdsdsffffffffffffffffffffffffffffffffffffffffffffffffffff");

        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);

        assertThat("{\"description\":\"Максимальная длина описания — 200 символов\"}").isEqualTo(response.getBody().toString());
        assertThat(HttpStatus.BAD_REQUEST).isEqualTo(response.getStatusCode());
    }

    @Test
    @DisplayName("проверяем добавления фильма, если дата раньше 28 декабря 1895 года")
    void badRequestCreateFilmWhenReleaseDateInvalidTest() {
        Film film = createFilm();
        film.setReleaseDate(LocalDate.of(1985, 10, 10));

        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);

        assertThat("Дата релиза — не раньше 28 декабря 1895 года").isEqualTo(response.getBody());
        assertThat(HttpStatus.BAD_REQUEST).isEqualTo(response.getStatusCode());
    }

    @Test
    @DisplayName("проверяем добавления фильма, если продолжительность фильма отрицательное число")
    void badRequestCreateFilmWhenDurationInvalidTest() {
        Film film = createFilm();
        film.setDuration(Duration.ofMinutes(-120));

        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);

        assertThat("Продолжительность фильма должна быть положительным числом").isEqualTo(response.getBody());
        assertThat(HttpStatus.BAD_REQUEST).isEqualTo(response.getStatusCode());
    }

    @Test
    @DisplayName("Проверяем получение фильма")
    void getFilm() {
        Film film = createFilm();
        filmStorage.create(film);
        ResponseEntity<String> response = restTemplate.getForEntity("/films/1", String.class);
        assertThat(response).isNotNull();
        assertThat("{\"id\":1,\"name\":\"testName\",\"description\":\"тестофый фильм\",\"releaseDate\":\"1991-02-01\",\"duration\":\"PT2H\",\"filmLikes\":[]}").isEqualTo(response.getBody());
    }

    @Test
    @DisplayName("Проверяем добавление лайка.")
    void likedFilms() {
        Film film = createFilm();
        filmStorage.create(film);

        ResponseEntity<String> response = restTemplate.exchange(
                "/films/1/like/1",
                HttpMethod.PUT,
                null,
                String.class
        );
        Film updatedFilm = filmStorage.getFilm(1L).get();

        assertThat("Лайк успешно поставлен").isEqualTo(response.getBody());
        assertThat(updatedFilm.getFilmLikes().contains(1L)).isTrue();
    }

    @Test
    @DisplayName("Проверяем удаление лайка.")
    void deleteLike() {
        Film film = createFilm();
        film.getFilmLikes().add(1L);
        filmStorage.create(film);

        ResponseEntity<String> response = restTemplate.exchange(
                "/films/1/like/1",
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertThat("Лайк успешно удален.").isEqualTo(response.getBody());
        assertThat(0).isEqualTo(filmStorage.getFilm(1L).get().getFilmLikes().size());
    }

    @Test
    @DisplayName("Проверяем получение популярных фильмов.")
    void getPopularFilms() {
        Film film1 = createFilm();
        film1.setFilmLikes(new HashSet<>(Arrays.asList(1L, 2L, 3L)));

        Film film2 = createFilm();
        film2.setId(2L);
        film2.setFilmLikes(new HashSet<>(Arrays.asList(1L, 2L)));

        Film film3 = createFilm();
        film3.setId(3L);

        filmStorage.create(film1);
        filmStorage.create(film2);
        filmStorage.create(film3);

        ResponseEntity<List<Film>> response = restTemplate.exchange(
                "/films/popular",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        List<Film> popularFilm = response.getBody();

        assertThat(3).isEqualTo(popularFilm.size());
        assertThat(3).isEqualTo(popularFilm.get(0).getFilmLikes().size());
    }

    @Test
    @DisplayName("Проверка создания фильма если дата релиза невалидна.")
    void createFilmWithInvalidReleaseDate() {
        Film film = createFilm();
        film.setReleaseDate(LocalDate.of(1895, 1, 1));

        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);

        assertThat("Дата релиза — не раньше 28 декабря 1895 года").isEqualTo(response.getBody());
    }

    @Test
    @DisplayName("Проверка создания фильма если продолжительность отрицательная.")
    void createFilmWithNegativeDuration() {
        Film film = createFilm();
        film.setDuration(Duration.ofMinutes(-120));

        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);

        assertThat("Продолжительность фильма должна быть положительным числом").isEqualTo(response.getBody());
    }

}