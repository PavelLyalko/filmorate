package ru.yandex.practicum.Filmorate.model;

import lombok.Data;
import ru.yandex.practicum.Filmorate.model.enums.FriendStatus;

import java.time.LocalDate;
import java.util.Map;

@Data
public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Map<Long, FriendStatus> friends;

    public void addFriend(Long id, FriendStatus status) {
        this.friends.put(id, status);
    }

    public void deleteFriend(Long id) {
        this.friends.remove(id);
    }
}
