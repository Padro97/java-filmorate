package ru.yandex.practicum.filmorate.controller;


import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("/films")
public class FilmController {
    private List<Film> films = new ArrayList<>();
    private final Logger log = LoggerFactory.getLogger(FilmController.class);

    @PostMapping("/add")
    public Film addFilm(@RequestBody Film film) {
        try {
            if (film.getName() == null || film.getName().isEmpty()) {
                log.error("Ошибка: Название фильма не может быть пустым");
                return null;
            }

            if (film.getDescription() != null && film.getDescription().length() > 200) {
                log.error("Ошибка: Максимальная длина описания - 200 символов");
                return null;
            }

            if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 20))) {
                log.error("Ошибка: Дата релиза не может быть раньше 28 декабря 1895 года");
                return null;
            }

            if (film.getDuration() <= 0) {
                log.error("Ошибка: Продолжительность фильма должна быть положительной");
                return null;
            }

            film.setId(films.size() + 1);
            films.add(film);
            log.info("Фильм успешно добавлен: {}", film);
            return film;
        } catch (Exception e) {
            log.error("Произошла ошибка при добавлении фильма", e);
            return null;
        }
    }

    @PutMapping("/update/{id}")
    public Film updateFilm(@PathVariable int id, @RequestBody Film updatedFilm) {
        try {
            for (Film film : films) {
                if (film.getId() == id) {
                    film.setName(updatedFilm.getName());
                    film.setDescription(updatedFilm.getDescription());
                    film.setReleaseDate(updatedFilm.getReleaseDate());
                    film.setDuration(updatedFilm.getDuration());
                    log.info("Фильм успешно обновлен: {}", film);
                    return film;
                }
            }

            log.error("Ошибка: Фильм с указанным ID не найден");
            return null;
        } catch (Exception e) {
            log.error("Произошла ошибка при обновлении фильма", e);
            return null;
        }
    }

    @GetMapping("/all")
    public List<Film> getAllFilms() {
        return films;
    }
}
