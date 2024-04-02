package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.intrfc.GenreStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> getGenres() {
        Collection<Genre> genres = new ArrayList<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES");

        while (genreRows.next()) {
            Genre genre = new Genre();
            genre.setId(genreRows.getInt("GENRE_ID"));
            genre.setName(genreRows.getString("GENRE_NAME"));
            genres.add(genre);
        }
        return genres;
    }

    @Override
    public Genre getGenreById(int genreId) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES WHERE GENRE_ID = ?", genreId);
        return makeGenre(genreRows);
    }

    private Genre makeGenre(SqlRowSet genreRows) {
        if (genreRows.next()) {
            Genre genre = new Genre();
            genre.setId(genreRows.getInt("GENRE_ID"));
            genre.setName(genreRows.getString("GENRE_NAME"));
            return genre;
        } else {
            throw new NotFoundException("Жанр не найден.");
        }
    }
}
