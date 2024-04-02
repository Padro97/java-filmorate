package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.builders.RatingBuilder;
import ru.yandex.practicum.filmorate.dao.intrfc.RatingStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class RatingDbStorage implements RatingStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Rating> getRating() {
        Collection<Rating> ratings = new ArrayList<>();
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet("SELECT * FROM RATINGS");

        while (ratingRows.next()) {
            ratings.add(RatingBuilder.buildRatingFromSqlRowSet(ratingRows));
        }
        return ratings;
    }

    @Override
    public Rating getRatingById(int ratingId) {

        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet("SELECT * FROM RATINGS WHERE RATING_ID = ?", ratingId);
        return makeRating(ratingRows);
    }

    private Rating makeRating(SqlRowSet ratingRows) {
        if (ratingRows.next()) {
            Rating rating = new Rating();
            rating.setId(ratingRows.getInt("RATING_ID"));
            rating.setName(ratingRows.getString("RATING_NAME"));
            return rating;
        } else {
            throw new NotFoundException("Рейтинг не найден.");
        }
    }
}