package ru.yandex.practicum.Filmorate.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.Filmorate.FilmorateTests;
import ru.yandex.practicum.Filmorate.exception.ValidationException;
import ru.yandex.practicum.Filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryUserStorageTest extends FilmorateTests {

    @Test
    @DisplayName("Проверка создания пользовалтеля с валидными данными.")
    void createUserWithValidData() {
        User user = createUser();
        assertDoesNotThrow(() -> userStorage.create(user));
    }

    @Test
    @DisplayName("Проверка создание пользовтеля с невалидным Email.")
    void createUserWithInvalidEmail() {
        User user = createUser();
        user.setEmail("invalidEmail");
        ValidationException exception = assertThrows(ValidationException.class, () -> userStorage.create(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ '@'.", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка создание пользовтеля с невалидным логином.")
    void createUserWithInvalidLogin() {
        User user = createUser();
        user.setLogin("invalid login");
        ValidationException exception = assertThrows(ValidationException.class, () -> userStorage.create(user));
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка создание пользовтеля с пустым именем.")
    void createUserWithEmptyName() {
        User user = createUser();
        user.setName("");
        assertDoesNotThrow(() -> userStorage.create(user));
        assertEquals("testLogin", user.getName());
    }

    @Test
    @DisplayName("Проверка создание пользовтеля с датой рождения в бюдующем.")
    void createUserWithFutureBirthday() {
        User user = createUser();
        user.setBirthday(LocalDate.now().plusDays(1));
        ValidationException exception = assertThrows(ValidationException.class, () -> userStorage.create(user));
        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка обновления пользовтеля с валидными данными.")
    void updateUserWithValidData() {
        User user = createUser();
        userStorage.create(user);
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setLogin("updatedLogin");
        updatedUser.setName("Updated Name");
        updatedUser.setBirthday(LocalDate.of(1991, 1, 1));

        assertDoesNotThrow(() -> userStorage.update(updatedUser));
        assertEquals("updated@example.com", userStorage.getUser(1L).getEmail());
    }

    @Test
    @DisplayName("Проверка получения всех пользователей")
    void getAllUsers() {
        User user = createUser();
        userStorage.create(user);
        Collection<User> users = userStorage.getUsers();
        assertFalse(users.isEmpty());
    }

    @Test
    @DisplayName("Проверка получения пользователя по id.")
    void getUserById() {
        User user = createUser();
        userStorage.create(user);
        User retrievedUser = userStorage.getUser(1L);
        assertNotNull(retrievedUser);
        assertEquals("test@example.com", retrievedUser.getEmail());
    }

    @Test
    @DisplayName("Проверка добавления друзей")
    void addFriend() {
        User user1 = createUser();
        User user2 = createUser();
        user2.setId(2L);
        userStorage.create(user1);
        userStorage.create(user2);
        userService.addFriend(user1.getId(), user2.getId());
        assertTrue(user1.getFriends().contains(user2.getId()));
        assertTrue(user2.getFriends().contains(user1.getId()));
    }

    @Test
    @DisplayName("Проверка удаления друзей")
    void deleteFriend() {
        User user1 = createUser();
        User user2 = createUser();
        user2.setId(2L);
        user1.getFriends().add(user2.getId());
        user2.getFriends().add(user1.getId());
        userStorage.create(user1);
        userStorage.create(user2);

        userService.deleteFriend(user1.getId(), user2.getId());
        assertFalse(user1.getFriends().contains(user2.getId()));
        assertFalse(user2.getFriends().contains(user1.getId()));
    }
}
