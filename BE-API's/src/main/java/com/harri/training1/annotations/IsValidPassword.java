package com.harri.training1.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = PasswordValidator.class)
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsValidPassword {
    String message() default "Invalid password. It must contain at least one character, one number, and be at least 9 characters long.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
