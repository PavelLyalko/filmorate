package ru.yandex.practicum.Filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.Filmorate.model.User;
import ru.yandex.practicum.Filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public void addFriend(Long user1Id, Long user2Id) {
        userStorage.getUser(user1Id).addFriend(user2Id);
        userStorage.getUser(user2Id).addFriend(user1Id);
    }

    @Override
    public void deleteFriend(Long user1Id, Long user2Id) {
        userStorage.getUser(user1Id).deleteFriend(user2Id);
        userStorage.getUser(user2Id).deleteFriend(user1Id);
    }

    @Override
    public List<User> getAllFriends(Long id) {
        Set<Long> friendIds = userStorage.getUser(id).getFriends();
        Collection<User> users = userStorage.getUsers();

        return users.stream()
                .filter(user -> friendIds.contains(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getEachFriendList(Long user1Id, Long user2Id) {
        Set<Long> user1Friend = userStorage.getUser(user1Id).getFriends();
        Set<Long> user2Friend = userStorage.getUser(user2Id).getFriends();

        return user1Friend.stream()
                .filter(user2Friend::contains)
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }
}
