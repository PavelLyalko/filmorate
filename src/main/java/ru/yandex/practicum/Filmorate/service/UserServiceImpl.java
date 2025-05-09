package ru.yandex.practicum.Filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.Filmorate.model.User;
import ru.yandex.practicum.Filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserStorage userStorage;

    @Override
    public void addFriend(Long user1Id, Long user2Id) {
        userStorage.getUser(user1Id).getFriends().add(user2Id);
        userStorage.getUser(user2Id).getFriends().add(user1Id);
    }

    @Override
    public void deleteFriend(Long user1Id, Long user2Id) {
        userStorage.getUser(user1Id).getFriends().remove(user2Id);
        userStorage.getUser(user2Id).getFriends().remove(user1Id);
    }

    @Override
    public List<User> getAllFriends(Long id) {
        List<User> users = new ArrayList<>();
        List<User> temp = userStorage.getUsers().stream().toList();
        for (Long currentId : userStorage.getUser(id).getFriends()) {
            for (User user : temp) {
                if (Objects.equals(user.getId(), currentId)) {
                    users.add(user);
                }
            }
        }

        return users;
    }

    @Override
    public List<User> getEachFriendList(Long user1Id, Long user2Id) {
        Set<Long> user1Friend = userStorage.getUser(user1Id).getFriends();
        Set<Long> user2Friend = userStorage.getUser(user2Id).getFriends();
        List<User> friendUser = new ArrayList<>();
        for (Long id : user1Friend) {
            if (user2Friend.contains(id)) {
                friendUser.add(userStorage.getUser(id));
            }
        }
        return friendUser;
    }
}
