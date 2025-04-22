package ru.yandex.practicum.Filmorate.contoller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.Filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmControllerTest {
    private final String testText = "fdfdsdsfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffdfdsdsfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffdfdsdsffffffffffffffffffffffffffffffffffffffffffffffffffff";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Проверка успешного добавления фильма, если все поля валидны")
    void successCreateUserTest() {
        Film film = createFilm();

        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody(), film);
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
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("[{id=1, name=testName, description=тестофый фильм, releaseDate=1991-02-01, duration=PT2H}, {id=2, name=testName, description=тестофый фильм, releaseDate=1991-02-01, duration=PT2H}]", response.getBody().toString());
    }

    @ValueSource(strings = {"null", ""})
    @ParameterizedTest
    @DisplayName("Проеверяет добавление фильма с пустым названием")
    void badRequestCreateFilmWhenNameInvalidTest(String name) {
        Film film = createFilm();
        String newName = "null".equals(name) ? null : name;
        film.setName(newName);

        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);

        assertEquals("Название не может быть пустым.", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @ValueSource(strings = {"null", testText})
    @ParameterizedTest
    @DisplayName("проверяем добавления, если описание больше 200 символов")
    void badRequestCreateFilmWhenDescriptionInvalidTest(String description) {
        Film film = createFilm();
        String desc = "null".equals(description) ? null : description;
        film.setDescription(desc);

        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);

        assertEquals("Максимальная длина описания — 200 символов", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("проверяем добавления фильма, если дата раньше 28 декабря 1895 года")
    void badRequestCreateFilmWhenReleaseDateInvalidTest() {
        Film film = createFilm();
        film.setReleaseDate(LocalDate.of(1985, 10, 10));

        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);

        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("проверяем добавления фильма, если продолжительность фильма отрицательное число")
    void badRequestCreateFilmWhenDurationInvalidTest() {
        Film film = createFilm();
        film.setDuration(Duration.ofMinutes(-120));

        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);

        assertEquals("Продолжительность фильма должна быть положительным числом", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private Film createFilm() {
        Film film = new Film();
        film.setId(1L);
        film.setDescription("тестофый фильм");
        film.setDuration(Duration.ofMinutes(120));
        film.setName("testName");
        film.setReleaseDate(LocalDate.of(1991, 2, 1));

        return film;
    }
}