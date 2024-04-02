package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.builders.FilmBuilder;
import ru.yandex.practicum.filmorate.dao.intrfc.LikeStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(int filmId, int userId) {
        String sql = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String sql = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT F.* FROM " +
                "(SELECT FILM_ID " +
                "FROM LIKES " +
                "GROUP BY FILM_ID " +
                "ORDER BY COUNT(USER_ID) DESC " +
                "LIMIT ?) AS L " +
                "INNER JOIN FILMS AS F ON L.FILM_ID=F.FILM_ID";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, count);

        while (filmRows.next()) {
            films.add(FilmBuilder.buildFilmFromSqlRowSet(filmRows));
        }
        return films;
    }
}
