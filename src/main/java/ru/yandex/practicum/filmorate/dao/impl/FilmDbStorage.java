package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> findAll() {
        Collection<Film> films = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS");

        while (filmRows.next()) {
            Film film = new Film();
            film.setId(filmRows.getInt("FILM_ID"));
            film.setName(filmRows.getString("FILM_NAME"));
            film.setDescription(filmRows.getString("DESCRIPTION"));
            film.setReleaseDate(filmRows.getDate("RELEASE_DATE").toString());
            film.setDuration(filmRows.getInt("DURATION"));
            films.add(film);
        }
        return films;
    }

    @Override
    public Film createFilm(Film film) {
        String sql = "INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID)" +
                " VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        film.setId(id);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE FILM_ID = ?", film.getId());

        if (filmRows.next()) {
            String sql = "UPDATE FILMS SET FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?," +
                    " RATING_ID = ? WHERE FILM_ID = ?";

            jdbcTemplate.update(sql, film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                    film.getDuration(), film.getMpa().getId(), film.getId());
        } else {
            throw new NotFoundException("Фильм c id = " + film.getId() + " не найден.");
        }

        return film;
    }

    @Override
    public void deleteFilm(int id) {
        String sql = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Film findFilm(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE FILM_ID = ?", id);
        return makeFilm(filmRows);
    }

    public Film makeFilm(SqlRowSet filmRows) {
        if (filmRows.next()) {
            Film film = new Film();
            film.setId(filmRows.getInt("FILM_ID"));
            film.setName(filmRows.getString("FILM_NAME"));
            film.setDescription(filmRows.getString("DESCRIPTION"));
            film.setReleaseDate(filmRows.getDate("RELEASE_DATE").toString());
            film.setDuration(filmRows.getInt("DURATION"));
            Rating rating = new Rating();
            rating.setId(filmRows.getInt("RATING_ID"));
            film.setMpa(rating);
            return film;
        } else {
            throw new NotFoundException("Фильм не найден.");
        }
    }
}
