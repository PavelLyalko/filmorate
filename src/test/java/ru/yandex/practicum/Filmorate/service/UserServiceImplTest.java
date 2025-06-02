package ru.yandex.practicum.Filmorate.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.Filmorate.FilmorateTests;
import ru.yandex.practicum.Filmorate.model.User;
import ru.yandex.practicum.Filmorate.model.enums.FriendStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceImplTest extends FilmorateTests {
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

        assertThat(1).isEqualTo(friends.size());
        assertThat(user2.getId()).isEqualTo(friends.get(0).getId());
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

        assertThat(1).isEqualTo(commonFriends.size());
        assertThat(user1.getId()).isEqualTo(commonFriends.get(0).getId());
    }
}
