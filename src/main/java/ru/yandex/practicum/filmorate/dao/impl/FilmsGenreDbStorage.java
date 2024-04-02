package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.intrfc.FilmsGenreStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FilmsGenreDbStorage implements FilmsGenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Map<Integer, List<Integer>> getGenresOfFilmsById(Collection<Film> films) {
        String sql = getSqlString(films);
        SqlRowSet genreIdRows = jdbcTemplate.queryForRowSet(sql);
        Map<Integer, List<Integer>> filmsWithGenres = new HashMap<>();

        while (genreIdRows.next()) {
            List<Integer> genreId = filmsWithGenres.getOrDefault(genreIdRows.getInt("FILM_ID"),
                    new ArrayList<>());
            genreId.add(genreIdRows.getInt("GENRE_ID"));
            filmsWithGenres.put(genreIdRows.getInt("FILM_ID"), genreId);
        }
        return filmsWithGenres;
    }

    @Override
    public void setFilmGenres(Film film) {
        if (film.getGenres() != null) {
            String sqlGenres = "MERGE INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
            List<Genre> genres = new ArrayList<>(film.getGenres());

            jdbcTemplate.batchUpdate(sqlGenres,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                            preparedStatement.setInt(1, film.getId());
                            preparedStatement.setInt(2, genres.get(i).getId());
                        }

                        @Override
                        public int getBatchSize() {
                            return film.getGenres().size();
                        }
                    });
        }
    }

    private String getSqlString(Collection<Film> films) {
        List<Integer> filmsId = new ArrayList<>();

        for (Film film : films) {
            filmsId.add(film.getId());
        }

        String numbers = "";

        if (!filmsId.isEmpty()) {
            numbers += filmsId.get(0);
            if (filmsId.size() > 1) {
                for (int i = 1; i < filmsId.size(); i++) {
                    numbers += ", " + filmsId.get(i);
                }
            }
        } else {
            throw new NotFoundException("Список жанров+ пуст");
        }

        String sql = "SELECT * FROM FILM_GENRES WHERE FILM_ID IN (" + numbers + ")";
        return sql;
    }
}
