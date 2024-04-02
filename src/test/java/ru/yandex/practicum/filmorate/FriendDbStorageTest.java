package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.intrfc.FriendStorage;
import ru.yandex.practicum.filmorate.dao.impl.FriendDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FriendDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void shouldReturnFriends() {
        FriendStorage friendStorage = new FriendDbStorage(jdbcTemplate);
        assertEquals(1, friendStorage.getFriends(1).size());
    }

    @Test
    public void shouldReturnCommonFriends() {
        UserStorage userStorage = new UserDbStorage(jdbcTemplate);
        FriendStorage friendStorage = new FriendDbStorage(jdbcTemplate);
        assertEquals(userStorage.findUser(3), friendStorage.getCommonFriends(1, 2).get(0));
    }

    @Test
    public void shouldAddFriend() {
        UserStorage userStorage = new UserDbStorage(jdbcTemplate);
        FriendStorage friendStorage = new FriendDbStorage(jdbcTemplate);
        friendStorage.addFriend(3,1);
        User user = friendStorage.getFriends(3).get(0);
        friendStorage.deleteFriend(3,1);
        assertEquals(userStorage.findUser(1), user);
    }
}