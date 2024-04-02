package ru.yandex.practicum.filmorate.dao.intrfc;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface LikeStorage {
    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    Collection<Film> getPopularFilms(int count);
}
