package ru.yandex.practicum.Filmorate.service;

import ru.yandex.practicum.Filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {

    void deleteFriend(Long fromUserId, Long toUserId);

    User getUser(Long id);

    List<User> getAllFriends(Long id);

    List<User> getEachFriendList(Long fromUserId, Long toUserId);

    void create(User user);

    void update(User updateUser);

    Collection<User> getUsers();

    void sendFriendRequest(Long userId, Long friendId);

    void confirmFriendship(Long userId, Long friendId);

    void deleteAllUsers();
}
