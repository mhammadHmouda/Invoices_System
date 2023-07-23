package com.harri.training1.annotations;

import com.harri.training1.exceptions.IsValidPasswordException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.annotation.Configuration;
import java.util.regex.Pattern;

@Configuration
public class PasswordValidator implements ConstraintValidator<IsValidPassword, String> {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{9,}$");

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        boolean valid = PASSWORD_PATTERN.matcher(password).matches();

        if(!valid)
            throw new IsValidPasswordException("Invalid password. It must contain at least one character, one number, and be at least 9 characters long.");

        return true;
    }
}

