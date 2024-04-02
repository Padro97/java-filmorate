package ru.yandex.practicum.filmorate.builders;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmBuilder {
    public static Film buildFilmFromSqlRowSet(SqlRowSet rowSet) {
        Film film = new Film();
        film.setId(rowSet.getInt("FILM_ID"));
        film.setName(rowSet.getString("FILM_NAME"));
        film.setDescription(rowSet.getString("DESCRIPTION"));
        film.setReleaseDate(rowSet.getDate("RELEASE_DATE").toString());
        film.setDuration(rowSet.getInt("DURATION"));
        Rating rating = new Rating();
        rating.setId(rowSet.getInt("RATING_ID"));
        film.setMpa(rating);
        return film;
    }
}

