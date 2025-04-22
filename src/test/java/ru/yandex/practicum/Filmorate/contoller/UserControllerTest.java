package ru.yandex.practicum.Filmorate.contoller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.Filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Проверка успешного добавления пользователя, если все поля валидны")
    void successCreateUserTest() {
        User user = createUser();

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody(), user);
    }

    @Test
    @DisplayName("Проверка получения всех пользователей")
    void successGetAllUsersTest() {
        User user1 = createUser();
        User user2 = createUser();
        user2.setId(2L);

        restTemplate.postForEntity("/users", user1, User.class);
        restTemplate.postForEntity("/users", user2, User.class);
        ResponseEntity<Collection> response = restTemplate.getForEntity("/users", Collection.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("[{id=1, email=test@example.com, login=testLogin, name=testName, birthday=1990-01-01}, {id=2, email=test@example.com, login=testLogin, name=testName, birthday=1990-01-01}]", response.getBody().toString());
    }

    @CsvSource({"example.com", "null"})
    @ParameterizedTest
    @DisplayName("Проверка добавления, если почта не содержит '@'")
    void badRequestCreateUserWhenEmailInvalidTest(String email) {
        User user = createUser();
        user.setEmail(email);

        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);

        assertEquals("Электронная почта не может быть пустой и должна содержать символ '@'.", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @CsvSource({"null", "gf gfg",})
    @ParameterizedTest
    @DisplayName("проверяем добавления, если логин пустой или содержит пробелы")
    void badRequestCreateUserWhenLoginInvalidTest(String login) {
        User user = createUser();
        String log = "null".equals(login) ? null : login;
        user.setLogin(log);

        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);

        assertEquals("Логин не может быть пустым и содержать пробелы.", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    @DisplayName("Проверка добвалнеия подьзовалтеля если дата рождения невалидная")
    void badRequestCreateUserWhenBirthdayInvalidTest() {
        User user = createUser();
        user.setBirthday(LocalDate.of(2026, 10, 20));

        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);

        assertEquals("Дата рождения не может быть в будущем", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLogin("testLogin");
        user.setName("testName");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        return user;
    }
}
