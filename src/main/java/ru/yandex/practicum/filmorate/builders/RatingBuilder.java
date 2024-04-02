package ru.yandex.practicum.filmorate.builders;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

public class RatingBuilder {
    public static Rating buildRatingFromSqlRowSet(SqlRowSet rowSet) throws NotFoundException {
        Rating rating = new Rating();
        rating.setId(rowSet.getInt("RATING_ID"));
        rating.setName(rowSet.getString("RATING_NAME"));
        return rating;
    }
}
