package ru.yandex.practicum.Filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.Filmorate.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.Filmorate.service.UserService;
import ru.yandex.practicum.Filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @PostMapping
    public User create(@RequestBody User user) {
        userStorage.create(user);
        log.debug("Пользователь с id {} успешно добавлен", user.getId());

        return user;
    }

    @PutMapping
    public User update(@RequestBody User updateUser) {
        userStorage.update(updateUser);
        log.debug("Пользователь с id {} успешно обновлен", updateUser.getId());

        return updateUser;
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.debug("Получение всех пользователей: {}", userStorage.getUsers());
        return userStorage.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        log.debug("получения пользователя по Id: {}", id);
        return userStorage.getUser(Long.parseLong(id));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> addUserInFriendList(@PathVariable String id, @PathVariable String friendId) {
        userService.addFriend(Long.parseLong(id), Long.parseLong(friendId));
        return new ResponseEntity<>("Новый друг успешно добавлен", HttpStatus.OK);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> removeUserFromFriendList(@PathVariable String id, @PathVariable String friendId) {
        log.debug("Пользователь с Id: {}, удалил пользователя с Id {} из друзей", id, friendId);
        userService.deleteFriend(Long.parseLong(id), Long.parseLong(friendId));
        return new ResponseEntity<>("Друг успешно удален", HttpStatus.OK);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriendList(@PathVariable String id) {
        return userService.getAllFriends(Long.parseLong(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getEachFriendList(@PathVariable String id, @PathVariable String otherId) {
        return userService.getEachFriendList(Long.parseLong(id), Long.parseLong(otherId));
    }
}
