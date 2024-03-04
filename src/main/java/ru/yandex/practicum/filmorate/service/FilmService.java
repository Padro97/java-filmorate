package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film != null) {
            film.addLike(userId);
        } else {
            throw new ObjectNotFoundException("Film not found");
        }
    }


    public void removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film != null) {
            film.removeLike(userId);
        } else {
            throw new ObjectNotFoundException("Film not found");
        }
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }






    public Film getFilmById(Long id) {
        Optional<Film> optionalFilm = filmStorage.getFilms().stream()
                .filter(film -> film.getId().equals(id))
                .findFirst();

        return optionalFilm.orElse(null);
    }


}

