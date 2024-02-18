package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("/films")
public class FilmController {
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

            film.setId(nextId++);
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
}
