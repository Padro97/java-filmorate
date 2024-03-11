package ru.yandex.practicum.filmorate.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class AfterOrEqualDateConstraintValidator implements ConstraintValidator<AfterOrEqualDate, String> {

    private String annotationDate;

    @Override
    public void initialize(AfterOrEqualDate date) {
        this.annotationDate = date.value();
    }

    @Override
    public boolean isValid(String targetDate, ConstraintValidatorContext cxt) {
        if (targetDate == null) {
            return false;
        }

        try {
            String[] annotationDateParts = annotationDate.split("-");
            String[] targetDateParts = targetDate.split("-");

            return LocalDate.of(Integer.parseInt(targetDateParts[0]),
                            Integer.parseInt(targetDateParts[1]), Integer.parseInt(targetDateParts[2]))
                    .isAfter(LocalDate.of(Integer.parseInt(annotationDateParts[0]),
                            Integer.parseInt(annotationDateParts[1]), Integer.parseInt(annotationDateParts[2])));
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
