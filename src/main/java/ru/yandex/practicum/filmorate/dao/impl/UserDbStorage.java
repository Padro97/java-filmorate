package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> findAll() {
        Collection<User> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS");

        while (userRows.next()) {
            User user = new User();
            user.setId(userRows.getInt("USER_ID"));
            user.setEmail(userRows.getString("EMAIL"));
            user.setLogin(userRows.getString("LOGIN"));
            user.setName(userRows.getString("USER_NAME"));
            user.setBirthday(userRows.getDate("BIRTHDAY").toLocalDate());
            users.add(user);
        }
        return users;
    }

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        int id = keyHolder.getKey().intValue();

        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE USER_ID = ?", id);
        return makeUser(userRows);
    }

    @Override
    public User updateUser(User user) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE USER_ID = ?", user.getId());

        if (userRows.next()) {
            String sql = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?";
            jdbcTemplate.update(sql, user.getEmail(), user.getLogin(),
                    user.getName(), user.getBirthday(), user.getId());
        } else {
            throw new UserNotFoundException("Пользователь c id= " + user.getId() + " не найден.");
        }

        SqlRowSet updatedUserRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE USER_ID = ?",
                user.getId());
        return makeUser(updatedUserRows);
    }

    @Override
    public void deleteUser(int id) {
        String sql = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public User findUser(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE USER_ID = ?", id);
        return makeUser(userRows);
    }

    public User makeUser(SqlRowSet userRows) {
        if (userRows.next()) {
            User user = new User();
            user.setId(userRows.getInt("USER_ID"));
            user.setEmail(userRows.getString("EMAIL"));
            user.setLogin(userRows.getString("LOGIN"));
            user.setName(userRows.getString("USER_NAME"));
            user.setBirthday(userRows.getDate("BIRTHDAY").toLocalDate());
            return user;
        } else {
            throw new UserNotFoundException("Пользователь не найден.");
        }
    }
}
