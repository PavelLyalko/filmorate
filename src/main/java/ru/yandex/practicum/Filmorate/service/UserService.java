package ru.yandex.practicum.Filmorate.service;

import ru.yandex.practicum.Filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {
    void addFriend(Long fromUserId, Long toUserId);

    void deleteFriend(Long fromUserId, Long toUserId);

    User getUser(Long id);

    List<User> getAllFriends(Long id);

    List<User> getEachFriendList(Long fromUserId, Long toUserId);

    void create(User user);

    void update(User updateUser);

    Collection<User> getUsers();
}
