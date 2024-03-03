package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmService filmService, FilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    private int nextId = 1;
    private List<Film> films = new ArrayList<>();
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, jsonSerializationContext) ->
                    new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .create();

    @PostMapping
    public ResponseEntity<String> addFilm(@RequestBody Film film) {
        try {
            if (film.getName() == null || film.getName().isEmpty()) {
                log.error("Ошибка: Название фильма не может быть пустым");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Invalid film name"));
            }

            if (film.getDescription() != null && film.getDescription().length() > 200) {
                log.error("Ошибка: Максимальная длина описания - 200 символов");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Invalid film description length"));
            }

            if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 20))) {
                log.error("Ошибка: Дата релиза не может быть раньше 28 декабря 1895 года");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Invalid release date"));
            }

            if (film.getDuration() <= 0) {
                log.error("Ошибка: Продолжительность фильма должна быть положительной");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Invalid film duration"));
            }

            film.setId((long) nextId++);
            films.add(film);
            log.info("Фильм успешно добавлен: {}", film);
            String jsonResponse = gson.toJson(film);
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            log.error("Произошла ошибка при добавлении фильма", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Internal Server Error"));
        }
    }

    @PutMapping
    public ResponseEntity<String> updateFilm(@RequestBody Film updatedFilm) {
        try {
            for (Film film : films) {
                if (film.getId() == updatedFilm.getId()) {
                    film.setName(updatedFilm.getName());
                    film.setDescription(updatedFilm.getDescription());
                    film.setReleaseDate(updatedFilm.getReleaseDate());
                    film.setDuration(updatedFilm.getDuration());
                    log.info("Фильм успешно обновлен: {}", film);
                    String jsonResponse = gson.toJson(film);
                    return ResponseEntity.ok(jsonResponse);
                }
            }

            log.error("Ошибка: Фильм с указанным ID не найден");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse("Film not found"));
        } catch (Exception e) {
            log.error("Произошла ошибка при обновлении фильма", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Internal Server Error"));
        }
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return films;
    }

    private String createErrorResponse(String errorMessage) {
        return gson.toJson(new ErrorResponse("Bad Request", errorMessage));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<String> likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        try {
            filmService.addLike(id, userId);
            return ResponseEntity.ok("Like added successfully");} catch (ValidationException e) {
            ErrorResponse errorResponse = new ErrorResponse("Bad Request", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(errorResponse));
        } catch (ObjectNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse("Not Found", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gson.toJson(errorResponse));
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", "Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(errorResponse));
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<String> unlikeFilm(@PathVariable Long id, @PathVariable Long userId) {
        try {
            filmService.removeLike(id, userId);
            return ResponseEntity.ok("Like removed successfully");
        } catch (ValidationException e) {
            ErrorResponse errorResponse = new ErrorResponse("Bad Request", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(errorResponse));
        } catch (ObjectNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse("Not Found", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gson.toJson(errorResponse));
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", "Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(errorResponse));
        }
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        try {
            List<Film> popularFilms = filmService.getPopularFilms(count);
            return ResponseEntity.ok(popularFilms);
        } catch (Exception e) {
            log.error("Error while fetching popular films", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}