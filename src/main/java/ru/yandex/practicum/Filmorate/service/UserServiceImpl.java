package ru.yandex.practicum.Filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.Filmorate.exception.NotFoundException;
import ru.yandex.practicum.Filmorate.model.User;
import ru.yandex.practicum.Filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public void addFriend(Long fromUserId, Long toUserId) {
        User fromUser = getUser(fromUserId);
        User toUser = getUser(toUserId);
        fromUser.addFriend(toUserId);
        toUser.addFriend(fromUserId);
    }

    @Override
    public void deleteFriend(Long fromUserId, Long toUserId) {
        User fromUser = getUser(fromUserId);
        User toUser = getUser(toUserId);
        fromUser.deleteFriend(toUserId);
        toUser.deleteFriend(fromUserId);
    }

    @Override
    public User getUser(Long id) {
        return userStorage.getUser(id).orElseThrow(() -> new NotFoundException("Не найден пользователь с Id: " + id));
    }

    @Override
    public List<User> getAllFriends(Long id) {
        Set<Long> friendIds = userStorage.getUser(id).get().getFriends();
        Collection<User> users = userStorage.getUsers();

        return users.stream()
                .filter(user -> friendIds.contains(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getEachFriendList(Long fromUserId, Long toUserId) {
        User fromUser = userStorage.getUser(fromUserId).get();
        User toUser = userStorage.getUser(toUserId).get();
        Set<Long> fromUserFriend = fromUser.getFriends();
        Set<Long> toUserFriend = toUser.getFriends();

        return fromUserFriend.stream()
                .filter(toUserFriend::contains)
                .map(user -> userStorage.getUser(user).get())
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
        userStorage.update(updateUser);
    }

    @Override
    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }
}
