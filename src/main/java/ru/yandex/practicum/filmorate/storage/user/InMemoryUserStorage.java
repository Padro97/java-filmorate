package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User createUser(User user) {
        user.setId(++id);

        log.trace("user: {}", user);

        users.put(user.getId(), user);

        if (users.get(user.getId()).getName() == null || users.get(user.getId()).getName().isEmpty()) {
            user.setName(user.getLogin());
            users.put(user.getId(), user);
        }

        return users.get(user.getId());
    }

    @Override
    public User updateUser(User user) {
        log.trace("user: {}", user);

        if (users.containsKey(user.getId())) {
            users.replace(user.getId(), users.get(user.getId()), user);
            if (users.get(user.getId()).getName() == null) {
                user.setName(user.getLogin());
                users.put(user.getId(), user);
            }
        } else {
            throw new NotFoundException("Нет такого пользователя для обновления");
        }

        return users.get(user.getId());
    }

    @Override
    public void deleteUser(int id) {
        if (users.containsKey(id)) {
            users.remove(id);
        } else {
            throw new NotFoundException("Нет пользователя с id " + id + " для удаления ");
        }
    }

    @Override
    public User findUser(int id) {
        User user = users.get(id);

        if (user == null) {
            throw new NotFoundException("Нет пользователя с id " + id);
        }

        return users.get(id);
    }
}