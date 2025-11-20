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
public class Telephone {

    private static final Logger logger = LoggerFactory.getLogger(Password.class);

    private static final Pattern TELEPHONE_PATTERN = Pattern.compile("^\\(\\d{2}\\) \\d{4,5}-\\d{4}$");

    private final String telephone;

    public static Telephone of(String telephone) {
        validate(telephone);

        return new Telephone(telephone);
    }

    private static void validate(String telephone) {
        validateIfIsEmpty(telephone);
        validateIfIsValid(telephone);
    }

    private static void validateIfIsEmpty(String telephone) {
        if (telephone == null || telephone.trim().isEmpty()) {
            logger.error("Telephone validation failed: telephone is null or empty");

            throw DomainExceptionFactory.emptyField("telephone");
        }
    }

    private static void validateIfIsValid(String telephone) {
        if (!TELEPHONE_PATTERN.matcher(telephone).matches()) {
            logger.error("Invalid telephone format");

            throw DomainExceptionFactory.invalidPattern(
                    "telephone",
                    TELEPHONE_PATTERN.pattern(),
                    "Invalid telephone format"
            );
        }
    }

    public String getValue() {
        return telephone;
    }

}
