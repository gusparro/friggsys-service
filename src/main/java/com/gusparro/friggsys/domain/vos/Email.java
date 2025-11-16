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
public class Email {

    private static final Logger logger = LoggerFactory.getLogger(Email.class);

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private final String email;

    public static Email of(String email) {
        validateIfIsEmpty(email);

        String normalized = email.trim().toLowerCase();

        validateIfIsValid(normalized);

        return new Email(email);
    }

    private static void validateIfIsEmpty(String email) {
        if (email == null || email.trim().isEmpty()) {
            logger.error("Email is null or empty");

            throw DomainExceptionFactory.emptyField("email");
        }
    }

    private static void validateIfIsValid(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            logger.error("Invalid email format");

            throw DomainExceptionFactory.invalidPattern(
                    "email",
                    EMAIL_PATTERN.pattern(),
                    "Invalid email format"
            );
        }
    }

    public String getValue() {
        return email;
    }

}
