package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(++id);

        log.trace("film: {}", film);

        films.put(film.getId(), film);

        return films.get(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        log.trace("film: {}", film);

        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), films.get(film.getId()), film);
        } else {
            throw new FilmNotFoundException("Нет такого фильма для обновления");
        }

        return films.get(film.getId());
    }

    @Override
    public void deleteFilm(int id) {
        if (films.containsKey(id)) {
            films.remove(id);
        } else {
            throw new FilmNotFoundException("Нет фильма с id " + id + " для удаления");
        }
    }

    @Override
    public Film findFilm(int id) {
        Film film = films.get(id);

        if (film == null) {
            throw new FilmNotFoundException("Нет фильма с id " + id);
        }

        return film;
    }
}