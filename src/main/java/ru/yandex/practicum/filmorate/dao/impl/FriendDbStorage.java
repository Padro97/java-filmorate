package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.intrfc.FriendStorage;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(int userId, int friendId) {
        String sql = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String deleteFriendSql = "DELETE FROM FRIENDS " +
                "WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(deleteFriendSql, userId, friendId);
    }

    @Override
    public List<User> getFriends(int userId) {
        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet(
                "SELECT * " +
                        "FROM USERS " +
                        "WHERE USER_ID IN" +
                        "(SELECT FRIEND_ID " +
                        "FROM FRIENDS " +
                        "WHERE USER_ID = ?)", userId);
        return getListOfUsers(friendsRows);
    }

    @Override
    public List<User> getCommonFriends(int userId, int friendId) {
        String sql = "SELECT * FROM USERS WHERE USER_ID IN" +
                "(SELECT U.FRIEND_ID " +
                "FROM (SELECT *" +
                "FROM FRIENDS " +
                "WHERE USER_ID = ?) AS U " +
                "INNER JOIN FRIENDS AS F ON F.FRIEND_ID = U.FRIEND_ID " +
                "WHERE F.USER_ID = ?)";
        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet(sql, userId, friendId);
        return getListOfUsers(friendsRows);
    }

    private List<User> getListOfUsers(SqlRowSet friendsRows) {
        List<User> friends = new ArrayList<>();

        while (friendsRows.next()) {
            User user = new User();
            user.setId(friendsRows.getInt("USER_ID"));
            user.setEmail(friendsRows.getString("EMAIL"));
            user.setLogin(friendsRows.getString("LOGIN"));
            user.setName(friendsRows.getString("USER_NAME"));
            user.setBirthday(friendsRows.getDate("BIRTHDAY").toLocalDate());
            friends.add(user);
        }
        return friends;
    }
}
