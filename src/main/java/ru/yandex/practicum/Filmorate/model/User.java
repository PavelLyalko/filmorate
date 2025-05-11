package ru.yandex.practicum.Filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends;

    public void addFriend(Long id) {
        this.friends.add(id);
    }

    public void deleteFriend(Long id) {
        this.friends.remove(id);
    }
}
