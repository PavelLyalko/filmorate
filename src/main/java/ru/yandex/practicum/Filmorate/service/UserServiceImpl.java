package ru.yandex.practicum.Filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.Filmorate.exception.NotFoundException;
import ru.yandex.practicum.Filmorate.model.User;
import ru.yandex.practicum.Filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public void addFriend(Long fromUserId, Long toUserId) {
        userStorage.getUser(fromUserId).get().addFriend(toUserId);
        userStorage.getUser(toUserId).get().addFriend(fromUserId);
    }

    @Override
    public void deleteFriend(Long fromUserId, Long toUserId) {
        userStorage.getUser(fromUserId).get().deleteFriend(toUserId);
        userStorage.getUser(toUserId).get().deleteFriend(fromUserId);
    }

    @Override
    public User getUser(Long id) {
        Optional<User> user = userStorage.getUser(id);
        if (user.isEmpty()){
            throw new NotFoundException("Не найден пользователь с Id: " + id);
        }
        return user.get();
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
}
