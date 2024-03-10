package ru.yandex.practicum.filmorate.annotations;

import ru.yandex.practicum.filmorate.annotations.AfterOrEqualDateConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AfterOrEqualDateConstraintValidator.class)
public @interface AfterOrEqualDate {
    String value();
    String message() default "{AfterOrEqualDate}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
