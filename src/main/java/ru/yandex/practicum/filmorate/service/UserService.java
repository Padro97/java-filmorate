package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.intrfc.FriendStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    private final FriendStorage friendStorage;
    private final UserStorage userStorage;

    public UserService(FriendStorage friendStorage, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.friendStorage = friendStorage;
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User findUser(int id) {
        return userStorage.findUser(id);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

    public void addFriend(int userId, int friendId) {
        userStorage.findUser(userId);
        userStorage.findUser(friendId);
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        userStorage.findUser(userId);
        userStorage.findUser(friendId);
        friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(int id) {
        userStorage.findUser(id);
        return friendStorage.getFriends(id);
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        userStorage.findUser(userId);
        userStorage.findUser(friendId);
        return friendStorage.getCommonFriends(userId, friendId);
    }
}