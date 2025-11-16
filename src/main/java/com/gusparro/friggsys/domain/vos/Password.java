package com.gusparro.friggsys.domain.vos;

import com.gusparro.friggsys.domain.exceptions.DomainExceptionFactory;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode
@AllArgsConstructor(access = PRIVATE)
public class Password {

    private static final Logger logger = LoggerFactory.getLogger(Password.class);

    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 50;

    private static final Pattern DIGIT_PATTERN = Pattern.compile(".*\\d.*");
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile(".*[A-Z].*");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile(".*[a-z].*");
    private static final Pattern SPECIAL_CHARACTER_PATTERN = Pattern.compile(".*[!@#$%^&*(),.?\":{}|<>].*");

    private final String password;

    public static Password ofRaw(String rawPassword) {
        validateRawPassword(rawPassword);

        return new Password(rawPassword);
    }

    public static Password ofHash(String hash) {
        if (hash == null || hash.trim().isEmpty()) {
            logger.error("Invalid hash provided: hash is null or empty");

            throw DomainExceptionFactory.invalid("Hash cannot be empty", "password");
        }

        return new Password(hash);
    }

    private static void validateRawPassword(String password) {
        validateIfIsEmpty(password);

        validateMinLength(password);

        validateMaxLength(password);

        hasSomeDigit(password);

        hasSomeUppercaseLetter(password);

        hasSomeLowercaseLetter(password);

        hasSomeSpecialCharacter(password);
    }

    private static void validateIfIsEmpty(String password) {
        if (password == null || password.trim().isEmpty()) {
            logger.error("Password validation failed: password is null or empty");

            throw DomainExceptionFactory.emptyField("password");
        }
    }

    private static void validateMinLength(String password) {
        if (password.length() < PASSWORD_MIN_LENGTH) {
            logger.error("Password validation failed: length {} is less than minimum {}",
                    password.length(), PASSWORD_MIN_LENGTH);

            throw DomainExceptionFactory.minLength("password", PASSWORD_MIN_LENGTH, password.length());
        }
    }

    private static void validateMaxLength(String password) {
        if (password.length() > PASSWORD_MAX_LENGTH) {
            logger.error("Password validation failed: length {} exceeds maximum {}",
                    password.length(), PASSWORD_MAX_LENGTH);

            throw DomainExceptionFactory.maxLength("password", PASSWORD_MAX_LENGTH, password.length());
        }
    }

    private static void hasSomeDigit(String password) {
        if (!DIGIT_PATTERN.matcher(password).matches()) {
            logger.error("Password validation failed: password does not contain digits");

            throw DomainExceptionFactory.invalidPattern(
                    "password",
                    DIGIT_PATTERN.pattern(),
                    "At least one digit (0-9)"
            );
        }
    }

    private static void hasSomeUppercaseLetter(String password) {
        if (!UPPERCASE_PATTERN.matcher(password).matches()) {
            logger.error("Password validation failed: password does not contain uppercase letters");

            throw DomainExceptionFactory.invalidPattern(
                    "password",
                    UPPERCASE_PATTERN.pattern(),
                    "At least one uppercase letter (A-Z)"
            );
        }
    }

    private static void hasSomeLowercaseLetter(String password) {
        if (!LOWERCASE_PATTERN.matcher(password).matches()) {
            logger.error("Password validation failed: password does not contain lowercase letters");

            throw DomainExceptionFactory.invalidPattern(
                    "password",
                    LOWERCASE_PATTERN.pattern(),
                    "At least one lowercase letter (a-z)"
            );
        }
    }

    private static void hasSomeSpecialCharacter(String password) {
        if (!SPECIAL_CHARACTER_PATTERN.matcher(password).matches()) {
            logger.error("Password validation failed: password does not contain special characters");

            throw DomainExceptionFactory.invalidPattern(
                    "password",
                    SPECIAL_CHARACTER_PATTERN.pattern(),
                    "At least one special character (!@#$%^&*(),.?\":{}|<>)"
            );
        }
    }

    public String getValue() {
        return password;
    }

}
