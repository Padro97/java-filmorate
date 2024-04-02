package ru.yandex.practicum.filmorate.dao.intrfc;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FilmsGenreStorage {
    Map<Integer, List<Integer>> getGenresOfFilmsById(Collection<Film> films);

    void setFilmGenres(Film film);
}
