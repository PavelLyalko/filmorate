package ru.yandex.practicum.Filmorate.service;

import ru.yandex.practicum.Filmorate.model.User;

import java.util.List;

public interface UserService {
    void addFriend(Long user1Id, Long user2Id);

    void deleteFriend(Long user1Id, Long user2Id);

    List<User> getAllFriends(Long id);

    List<User> getEachFriendList(Long user1Id, Long user2Id);
}
