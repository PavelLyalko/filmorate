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

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

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
        assertThat("[{id=1, name=testName, description=тестофый фильм, releaseDate=1991-02-01, duration=PT2H}, {id=2, name=testName, description=тестофый фильм, releaseDate=1991-02-01, duration=PT2H}]").isEqualTo(response.getBody().toString());
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