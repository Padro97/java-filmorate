package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userId, int friendId) {
        User thisUser = userStorage.findUser(userId);
        User friend = userStorage.findUser(friendId);

        thisUser.addFriend(friendId);
        friend.addFriend(userId);

        userStorage.updateUser(thisUser);
        userStorage.updateUser(friend);
    }

    public void deleteFriend(int userId, int friendId) {
        User thisUser = userStorage.findUser(userId);
        User friend = userStorage.findUser(friendId);

        thisUser.deleteFriend(friendId);
        friend.deleteFriend(userId);

        userStorage.updateUser(thisUser);
        userStorage.updateUser(friend);
    }

    public List<User> getFriends(int id) {
        User user = userStorage.findUser(id);

        if (user == null) {
            throw new UserNotFoundException("Нет пользователя с id" + id);
        }

        List<User> friends = new ArrayList<>();

        for (int friend : user.getFriends()) {
            friends.add(userStorage.findUser(friend));
        }
        return friends;
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        User thisUser = userStorage.findUser(userId);
        User friend = userStorage.findUser(friendId);
        List<User> sameFriends = new ArrayList<>();

        for (int id : thisUser.getFriends()) {
            if (friend.getFriends().contains(id)) {
                sameFriends.add(userStorage.findUser(id));
            }
        }

        return sameFriends;
    }
}