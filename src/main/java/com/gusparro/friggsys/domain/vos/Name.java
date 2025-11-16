package com.gusparro.friggsys.domain.vos;

import com.gusparro.friggsys.domain.exceptions.DomainExceptionFactory;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode
@AllArgsConstructor(access = PRIVATE)
public class Name {

    private static final Logger logger = LoggerFactory.getLogger(Name.class);

    private static final int NAME_MIN_LENGTH = 5;
    private static final int NAME_MAX_LENGTH = 100;

    private final String name;

    public static Name of(String name) {
        validateIfNameIsEmpty(name);

        validateNameMinLength(name);

        validateNameMaxLength(name);

        return new Name(name);
    }

    private static void validateIfNameIsEmpty(String name) {
        if (name == null || name.trim().isEmpty()) {
            logger.error("Name is null or empty");

            throw DomainExceptionFactory.emptyField("name");
        }
    }

    private static void validateNameMinLength(String name) {
        if (name.length() < NAME_MIN_LENGTH) {
            logger.error("Name validation failed: length {} is less than minimum {}",
                    name.length(), NAME_MIN_LENGTH);

            throw DomainExceptionFactory.minLength("name", NAME_MIN_LENGTH, name.length());
        }
    }

    private static void validateNameMaxLength(String name) {
        if (name.length() > NAME_MAX_LENGTH) {
            logger.error("Name length is greater than {}", NAME_MAX_LENGTH);

            throw DomainExceptionFactory.maxLength("name", NAME_MAX_LENGTH, name.length());
        }
    }

    public String getValue() {
        return name;
    }

}
