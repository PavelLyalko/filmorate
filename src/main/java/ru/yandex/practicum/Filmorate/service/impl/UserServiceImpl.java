package ru.yandex.practicum.Filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.Filmorate.exception.NotFoundException;
import ru.yandex.practicum.Filmorate.model.User;
import ru.yandex.practicum.Filmorate.repository.FriendshipDbStorage;
import ru.yandex.practicum.Filmorate.service.UserService;
import ru.yandex.practicum.Filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final FriendshipDbStorage friendshipDbStorage;

    @Override
    public void deleteFriend(Long fromUserId, Long toUserId) {
        User fromUser = getUser(fromUserId);
        User toUser = getUser(toUserId);
        fromUser.deleteFriend(toUserId);
        toUser.deleteFriend(fromUserId);
    }

    @Override
    public User getUser(Long id) {
        return userStorage.getUserById(id).orElseThrow(() -> new NotFoundException("Не найден пользователь с Id: " + id));
    }

    @Override
    public List<User> getAllFriends(Long id) {
        Set<Long> friendIds = userStorage.getUserById(id).get().getFriends();
        Collection<User> users = userStorage.getUsers();

        return users.stream()
                .filter(user -> friendIds.contains(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getEachFriendList(Long fromUserId, Long toUserId) {
        User fromUser = userStorage.getUserById(fromUserId).get();
        User toUser = userStorage.getUserById(toUserId).get();
        Set<Long> fromUserFriend = fromUser.getFriends();
        Set<Long> toUserFriend = toUser.getFriends();

        return fromUserFriend.stream()
                .filter(toUserFriend::contains)
                .map(user -> userStorage.getUserById(user).get())
                .collect(Collectors.toList());
    }

    @Override
    public void create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        userStorage.create(user);
    }

    @Override
    public void update(User updateUser) {
        checkUserExists(updateUser.getId());
        userStorage.update(updateUser);
    }

    @Override
    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    private void checkUserExists(Long userId) {
        if (userStorage.getUserById(userId).isEmpty()) {
            throw new NoSuchElementException("Пользователь не найден");
        }
    }

    public void sendFriendRequest(Long userId, Long friendId) {
        checkUserExists(userId);
        checkUserExists(friendId);
        friendshipDbStorage.sendFriendRequest(userId, friendId);
    }

    public void confirmFriendship(Long userId, Long friendId) {
        checkUserExists(userId);
        checkUserExists(friendId);
        if (!friendshipDbStorage.existsPendingRequest(friendId, userId)) {
            throw new IllegalStateException("Заявки нет");
        }
        friendshipDbStorage.confirmFriend(userId, friendId);
    }

    @Override
    public void deleteAllUsers() {

    }
}
