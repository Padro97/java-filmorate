package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    protected int id;
    @NotEmpty(message = "Почта не может быть пустой")
    @Email(message = "Почта не соответствует формату")
    protected String email;
    @Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы")
    @NotEmpty(message = "Логин не может быть пустым")
    protected String login;
    protected String name;
    @PastOrPresent(message = "День рождения не может быть в будущем")
    protected LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();

    public void addFriend(int id) {
        friends.add(id);
    }

    public void deleteFriend(int id) {
        friends.remove(id);
    }

    public Set<Integer> getFriends() {
        return new HashSet<>(friends);
    }
}