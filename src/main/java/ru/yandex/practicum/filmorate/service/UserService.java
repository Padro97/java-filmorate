package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Service
public class UserService {

    private final Map<Long, Set<Long>> friendships;
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.friendships = new HashMap<>();
        this.userStorage = userStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        friendships.computeIfAbsent(userId, k -> new HashSet<>()).add(friendId);
        friendships.computeIfAbsent(friendId, k -> new HashSet<>()).add(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        if (friendships.containsKey(userId)) {
            friendships.get(userId).remove(friendId);
        }
        if (friendships.containsKey(friendId)) {
            friendships.get(friendId).remove(userId);
        }
    }

    public List<Long> getCommonFriends(Long user1Id, Long user2Id) {
        Set<Long> user1Friends = friendships.getOrDefault(user1Id, new HashSet<>());
        Set<Long> user2Friends = friendships.getOrDefault(user2Id, new HashSet<>());

        return user1Friends.stream()
                .filter(user2Friends::contains)
                .collect(Collectors.toList());
    }

    public List<User> getFriends(Long userId) {
        if (friendships.containsKey(userId)) {
            Set<Long> friendIds = friendships.get(userId);
            List<User> friends = friendIds.stream()
                    .map(friendId -> userStorage.getUserById(friendId))
                    .filter(user -> user != null)
                    .collect(Collectors.toList());
            return friends;
        } else {
            throw new ObjectNotFoundException("User not found");
        }
    }

    private void validate(User user) {
        log.debug("validate({})", user);
        if (user.getBirthday().isAfter(LocalDate.now()) || user.getBirthday() == null) {
            throw new ValidationException(format("Incorrect user's birthday with identifier %d", user.getId()));
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException(format("Incorrect user's email with identifier %d", user.getId()));
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("User's name with identifier {} was set as {}", user.getId(), user.getName());
        }
        if (user.getLogin().isBlank() || user.getLogin().isEmpty()) {
            throw new ValidationException(format("Incorrect user's login with identifier %d", user.getId()));
        }
    }
}

