package ru.yandex.practicum.Filmorate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import ru.yandex.practicum.Filmorate.model.Film;
import ru.yandex.practicum.Filmorate.model.User;
import ru.yandex.practicum.Filmorate.service.FilmService;
import ru.yandex.practicum.Filmorate.service.UserService;
import ru.yandex.practicum.Filmorate.storage.FilmStorage;
import ru.yandex.practicum.Filmorate.storage.UserStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilmorateTests {
    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    protected UserStorage userStorage;
    @Autowired
    protected FilmStorage filmStorage;
    @Autowired
    protected FilmService filmService;
    @Autowired
    protected UserService userService;

    public static Film createFilm() {
        Film film = new Film();
        film.setId(1L);
        film.setDescription("тестофый фильм");
        film.setDuration(Duration.ofMinutes(120));
        film.setName("testName");
        film.setReleaseDate(LocalDate.of(1991, 2, 1));
        film.setFilmLikes(new HashSet<>());

        return film;
    }

    public static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLogin("testLogin");
        user.setName("testName");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        user.setFriends(new HashSet<>());

        return user;
    }
}
