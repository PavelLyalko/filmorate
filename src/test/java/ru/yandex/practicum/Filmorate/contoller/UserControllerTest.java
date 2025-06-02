package ru.yandex.practicum.Filmorate.contoller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.Filmorate.FilmorateTests;
import ru.yandex.practicum.Filmorate.model.User;
import ru.yandex.practicum.Filmorate.model.enums.FriendStatus;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest extends FilmorateTests {

    @Test
    @DisplayName("Проверка успешного добавления пользователя, если все поля валидны")
    void successCreateUserTest() {
        User user = createUser();

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(user);
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

        assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
        assertThat("[{id=1, email=test@example.com, login=testLogin, name=testName, birthday=1990-01-01, friends={}}, {id=2, email=test@example.com, login=testLogin, name=testName, birthday=1990-01-01, friends={}}]").isEqualTo(response.getBody().toString());
    }

    @CsvSource({"example.com", "null"})
    @ParameterizedTest
    @DisplayName("Проверка добавления, если почта не содержит '@'")
    void badRequestCreateUserWhenEmailInvalidTest(String email) {
        User user = createUser();
        user.setEmail(email);

        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);

        assertThat("Электронная почта не может быть пустой и должна содержать символ '@'.").isEqualTo(response.getBody());
        assertThat(HttpStatus.BAD_REQUEST).isEqualTo(response.getStatusCode());
    }

    @CsvSource({"null", "gf gfg",})
    @ParameterizedTest
    @DisplayName("проверяем добавления, если логин пустой или содержит пробелы")
    void badRequestCreateUserWhenLoginInvalidTest(String login) {
        User user = createUser();
        String log = "null".equals(login) ? null : login;
        user.setLogin(log);

        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);

        assertThat("Логин не может быть пустым и содержать пробелы.").isEqualTo(response.getBody());
        assertThat(HttpStatus.BAD_REQUEST).isEqualTo(response.getStatusCode());
    }


    @Test
    @DisplayName("Проверка добвалнеия подьзовалтеля если дата рождения невалидная")
    void badRequestCreateUserWhenBirthdayInvalidTest() {
        User user = createUser();
        user.setBirthday(LocalDate.of(2026, 10, 20));

        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);

        assertThat("Дата рождения не может быть в будущем").isEqualTo(response.getBody());
        assertThat(HttpStatus.BAD_REQUEST).isEqualTo(response.getStatusCode());
    }

    @Test
    @DisplayName("Проверка полчения пользоватепля по Id.")
    void testGetUser() {
        User user = createUser();
        user.setName("Новый user1");
        userStorage.create(user);

        ResponseEntity<User> response = restTemplate.exchange(
                "/users/1",
                HttpMethod.GET,
                null,
                User.class
        );

        assertThat(response).isNotNull();
        assertThat("Новый user1").isEqualTo(response.getBody().getName());
    }

    @Test
    @DisplayName("Проверка добаления друзей, когда один пользователь отправил запрос на добавление другого пользователя в друзья.")
    void testAddUserInFriendList() {
        User user1 = createUser();
        User user2 = createUser();
        user2.setId(2L);
        userStorage.create(user1);
        userStorage.create(user2);

        ResponseEntity<String> response = restTemplate.exchange(
                "/users/1/friends/2",
                HttpMethod.PUT,
                null,
                String.class
        );

        assertThat("Новый друг успешно добавлен").isEqualTo(response.getBody());
        assertThat(userStorage.getUserById(user1.getId()).get().getFriends().contains(user2.getId())).isTrue();
    }

    @Test
    @DisplayName("Проверка удаления из друзей.")
    void testRemoveUserFromFriendList() {
        User user1 = createUser();
        user1.setFriends(Set.of(2L));
        User user2 = createUser();
        user2.setFriends(Set.of(1L));
        user2.setId(2L);
        userStorage.create(user1);
        userStorage.create(user2);

        ResponseEntity<String> response = restTemplate.exchange(
                "/users/1/friends/2",
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertThat("Друг успешно удален").isEqualTo(response.getBody());
        assertThat(userStorage.getUserById(user1.getId()).get().getFriends().contains(user2.getId())).isFalse();
        assertThat(userStorage.getUserById(user2.getId()).get().getFriends().contains(user1.getId())).isFalse();
    }

    @Test
    @DisplayName("Провекра вывода списка друзей.")
    void testGetUserFriendList() {
        User user1 = createUser();
        User user2 = createUser();
        User user3 = createUser();
        user1.setFriends(Set.of(2L, 3L));
        user2.setFriends(Set.of(1L));
        user3.setFriends(Set.of(1L));
        user2.setId(2L);
        user3.setId(3L);
        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);

        ResponseEntity<List<User>> response = restTemplate.exchange(
                "/users/1/friends",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(2).isEqualTo(response.getBody().size());
        assertThat(2L).isEqualTo(response.getBody().get(0).getId());
        assertThat(3L).isEqualTo(response.getBody().get(1).getId());
    }

    @Test
    @DisplayName("Проверка вывода списка общих друзей.")
    void testGetEachFriendList() {
        User user1 = createUser();
        User user2 = createUser();
        User user3 = createUser();
        user1.setFriends(Set.of(2L));
        user2.setFriends(Set.of(1L, 3L));
        user3.setFriends(Set.of(2L));
        user2.setId(2L);
        user3.setId(3L);
        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);

        ResponseEntity<List<User>> response = restTemplate.exchange(
                "/users/1/friends/common/3",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(1).isEqualTo(response.getBody().size());
        assertThat(2L).isEqualTo(response.getBody().get(0).getId());
    }
}
