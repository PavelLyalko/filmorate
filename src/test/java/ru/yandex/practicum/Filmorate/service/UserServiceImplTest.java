package ru.yandex.practicum.Filmorate.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.Filmorate.FilmorateTests;
import ru.yandex.practicum.Filmorate.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceImplTest extends FilmorateTests {
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

    @Test
    @DisplayName("Проверка получения списка друзей")
    void getAllFriends() {
        User user1 = createUser();
        User user2 = createUser();
        user2.setId(2L);
        user1.getFriends().add(user2.getId());
        user2.getFriends().add(user1.getId());
        userStorage.create(user1);
        userStorage.create(user2);

        List<User> friends = userService.getAllFriends(user1.getId());
        assertEquals(1, friends.size());
        assertEquals(user2.getId(), friends.get(0).getId());
    }

    @Test
    @DisplayName("Проверка общего списка друзей")
    void getEachFriendList() {
        User user1 = createUser();
        User user2 = createUser();
        user2.setId(2L);
        User user3 = createUser();
        user3.setId(3L);
        user1.getFriends().add(user2.getId());
        user1.getFriends().add(user3.getId());
        user2.getFriends().add(user1.getId());
        user3.getFriends().add(user1.getId());
        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);

        List<User> commonFriends = userService.getEachFriendList(user2.getId(), user3.getId());
        assertEquals(1, commonFriends.size());
        assertEquals(user1.getId(), commonFriends.get(0).getId());
    }
}
