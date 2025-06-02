package ru.yandex.practicum.Filmorate.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.Filmorate.FilmorateTests;
import ru.yandex.practicum.Filmorate.model.User;


import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

class InMemoryUserStorageTest extends FilmorateTests {

    @Test
    @DisplayName("Проверка создания пользовалтеля с валидными данными.")
    void createUserWithValidData() {
        User user = createUser();

        assertThatNoException().isThrownBy(() -> userStorage.create(user));
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

        assertThatNoException().isThrownBy(() -> userStorage.update(updatedUser));
        assertThat(userStorage.getUserById(1L).get().getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    @DisplayName("Проверка получения всех пользователей")
    void getAllUsers() {
        User user = createUser();
        userStorage.create(user);
        Collection<User> users = userStorage.getUsers();

        assertThat(users).isNotEmpty();
    }

    @Test
    @DisplayName("Проверка получения пользователя по id.")
    void getUserByIdById() {
        User user = createUser();
        userStorage.create(user);
        User retrievedUser = userStorage.getUserById(1L).get();

        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Проверка добавления друзей")
    void addFriend() {
        User user1 = createUser();
        User user2 = createUser();
        user2.setId(2L);
        userStorage.create(user1);
        userStorage.create(user2);

        userService.sendFriendRequest(user1.getId(), user2.getId());
        userService.sendFriendRequest(user2.getId(), user1.getId());

        assertThat(user1.getFriends()).contains(user2.getId());
        assertThat(user2.getFriends()).contains(user1.getId());
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

        assertThat(user1.getFriends()).doesNotContain(user2.getId());
        assertThat(user2.getFriends()).doesNotContain(user1.getId());
    }
    @Test
    void test(){
        assertThat(true).isTrue();
    }
}
