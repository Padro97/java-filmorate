package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindUserById() {
        User newUser = new User();
        newUser.setId(1);
        newUser.setEmail("user@email.ru");
        newUser.setLogin("padro");
        newUser.setName("Nikita Polyanin");
        newUser.setBirthday(LocalDate.of(2004, 11, 11));
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);

        User savedUser = userStorage.findUser(1);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testUpdateUser() {
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User user = userStorage.findUser(2);
        User updatedUser = new User();
        updatedUser.setId(2);
        updatedUser.setEmail("iv@ya.ru");
        updatedUser.setLogin("VANEK");
        updatedUser.setName("Ilia");
        updatedUser.setBirthday(LocalDate.of(1999, 9, 9));
        userStorage.updateUser(updatedUser);

        User savedUser = userStorage.findUser(2);

        userStorage.updateUser(user);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedUser);
    }

    @Test
    public void findAllUsers() {
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        Collection<User> dbUsers = userStorage.findAll();
        List<User> users = new ArrayList<>(dbUsers);
        assertTrue(users.contains(userStorage.findUser(1)));
        assertTrue(users.contains(userStorage.findUser(2)));
        assertTrue(users.contains(userStorage.findUser(3)));
        assertEquals(3, users.size());
    }

    @Test
    public void testDeleteUser() {
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User user = new User();
        user.setId(4);
        user.setEmail("delete@ya.ru");
        user.setLogin("DELETED");
        user.setName("DEL");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        userStorage.createUser(user);
        User savedUser = userStorage.findUser(4);
        userStorage.deleteUser(4);

        final UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        User savedUser = userStorage.findUser(4);
                    }
                });

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
        assertEquals("Пользователь не найден.", exception.getMessage());
    }
}