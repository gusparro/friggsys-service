package com.gusparro.friggsys.domain.vos;

import com.gusparro.friggsys.domain.exceptions.DomainExceptionFactory;
import com.gusparro.friggsys.domain.exceptions.ValidationError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;

@DisplayName("Password Value Object Tests")
class PasswordTest {

    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 50;

    @Test
    @DisplayName("Should create valid password")
    void shouldCreateValidPassword() {
        String validPassword = "ValidPass123!";

        Password password = Password.ofRaw(validPassword);

        assertThat(password).isNotNull();
        assertThat(password.getValue()).isEqualTo(validPassword);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Password123!",
            "SecurePass456@",
            "MyPass789#",
            "Test1234$%^&",
            "Complex!P@ssw0rd",
            "Aa1!bcdefgh"
    })
    @DisplayName("Should accept valid passwords")
    void shouldAcceptValidPasswords(String validPassword) {
        Password password = Password.ofRaw(validPassword);

        assertThat(password).isNotNull();
        assertThat(password.getValue()).isEqualTo(validPassword);
    }

    @Test
    @DisplayName("Should create password with min length")
    void shouldCreatePasswordWithMinLength() {
        String minPassword = "Pass123!";

        Password password = Password.ofRaw(minPassword);

        assertThat(password).isNotNull();
        assertThat(password.getValue()).isEqualTo(minPassword);
    }

    @Test
    @DisplayName("Should create password with max length")
    void shouldCreatePasswordWithMaxLength() {
        String maxPassword = "Pass123!" + "a".repeat(42);

        Password password = Password.ofRaw(maxPassword);

        assertThat(password).isNotNull();
        assertThat(password.getValue()).isEqualTo(maxPassword);
    }

    @Test
    @DisplayName("Should throw exception when password is null")
    void shouldThrowExceptionWhenPasswordIsNull() {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.emptyField("password"))
                    .thenThrow(new ValidationError("Password is null or empty"));

            assertThatThrownBy(() -> Password.ofRaw(null))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @Test
    @DisplayName("Should throw exception when password is empty")
    void shouldThrowExceptionWhenPasswordIsEmpty() {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.emptyField("password"))
                    .thenThrow(new ValidationError("Password is null or empty"));

            assertThatThrownBy(() -> Password.ofRaw(""))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @Test
    @DisplayName("Should throw exception when password is only whitespace")
    void shouldThrowExceptionWhenPasswordIsOnlyWhitespace() {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.emptyField("password"))
                    .thenThrow(new ValidationError("Password is null or empty"));

            assertThatThrownBy(() -> Password.ofRaw("   "))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @Test
    @DisplayName("Should throw exception when password is shorter than min length")
    void shouldThrowExceptionWhenPasswordIsShorterThanMinLength() {
        String shortPassword = "Pass12!";

        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.minLength("password", PASSWORD_MIN_LENGTH, 7))
                    .thenThrow(new ValidationError("Password is too short"));

            assertThatThrownBy(() -> Password.ofRaw(shortPassword))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @Test
    @DisplayName("Should throw exception when password is longer than max length")
    void shouldThrowExceptionWhenPasswordIsLongerThanMaxLength() {
        String longPassword = "Pass123!" + "a".repeat(43);

        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.maxLength("password", PASSWORD_MAX_LENGTH, 51))
                    .thenThrow(new ValidationError("Password is too long"));

            assertThatThrownBy(() -> Password.ofRaw(longPassword))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "PasswordWithoutDigit!",
            "Pass!Word",
            "NoDigitsHere!"
    })
    @DisplayName("Should throw exception when password has no digits")
    void shouldThrowExceptionWhenPasswordHasNoDigits(String passwordWithoutDigit) {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.invalidPattern(
                    "password",
                    ".*\\d.*",
                    "At least one digit (0-9)"
            )).thenThrow(new ValidationError("Password must contain at least one digit"));

            assertThatThrownBy(() -> Password.ofRaw(passwordWithoutDigit))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "password123!",
            "lowercase123!",
            "nouppercase1!"
    })
    @DisplayName("Should throw exception when password has no uppercase")
    void shouldThrowExceptionWhenPasswordHasNoUppercase(String passwordWithoutUppercase) {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.invalidPattern(
                    "password",
                    ".*[A-Z].*",
                    "At least one uppercase letter (A-Z)"
            )).thenThrow(new ValidationError("Password must contain at least one uppercase letter"));

            assertThatThrownBy(() -> Password.ofRaw(passwordWithoutUppercase))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "PASSWORD123!",
            "UPPERCASE123!",
            "NOLOWERCASE1!"
    })
    @DisplayName("Should throw exception when password has no lowercase")
    void shouldThrowExceptionWhenPasswordHasNoLowercase(String passwordWithoutLowercase) {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.invalidPattern(
                    "password",
                    ".*[a-z].*",
                    "At least one lowercase letter (a-z)"
            )).thenThrow(new ValidationError("Password must contain at least one lowercase letter"));

            assertThatThrownBy(() -> Password.ofRaw(passwordWithoutLowercase))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Password123",
            "NoSpecialChar1",
            "PlainPassword2"
    })
    @DisplayName("Should throw exception when password has no special character")
    void shouldThrowExceptionWhenPasswordHasNoSpecialCharacter(String passwordWithoutSpecialChar) {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.invalidPattern(
                    "password",
                    ".*[!@#$%^&*(),.?\":{}|<>].*",
                    "At least one special character (!@#$%^&*(),.?\":{}|<>)"
            )).thenThrow(new ValidationError("Password must contain at least one special character"));

            assertThatThrownBy(() -> Password.ofRaw(passwordWithoutSpecialChar))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @Test
    @DisplayName("Should create password from hash")
    void shouldCreatePasswordFromHash() {
        String hash = "$2a$10$Eq3Y8BG8gKJGYhKKE98U1eJrOJNzWpZJOAHEj/F8pN8qLQyFMXVLu";

        Password password = Password.ofHash(hash);

        assertThat(password).isNotNull();
        assertThat(password.getValue()).isEqualTo(hash);
    }

    @Test
    @DisplayName("Should throw exception when hash is null in of hash")
    void shouldThrowExceptionWhenHashIsNull() {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.invalid("Hash cannot be empty", "password"))
                    .thenThrow(new ValidationError("Invalid hash provided"));

            assertThatThrownBy(() -> Password.ofHash(null))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @Test
    @DisplayName("Should throw exception when hash is empty in of hash")
    void shouldThrowExceptionWhenHashIsEmpty() {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.invalid("Hash cannot be empty", "password"))
                    .thenThrow(new ValidationError("Invalid hash provided"));

            assertThatThrownBy(() -> Password.ofHash(""))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @Test
    @DisplayName("Should throw exception when hash is only whitespace in of hash")
    void shouldThrowExceptionWhenHashIsOnlyWhitespace() {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.invalid("Hash cannot be empty", "password"))
                    .thenThrow(new ValidationError("Invalid hash provided"));

            assertThatThrownBy(() -> Password.ofHash("   "))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @Test
    @DisplayName("Should be equal to another password with same value")
    void shouldBeEqualToAnotherPasswordWithSameValue() {
        String passwordValue = "ValidPass123!";

        Password password1 = Password.ofRaw(passwordValue);
        Password password2 = Password.ofRaw(passwordValue);

        assertThat(password1).isEqualTo(password2);
    }

    @Test
    @DisplayName("Should have same hashcode for equal passwords")
    void shouldHaveSameHashCodeForEqualPasswords() {
        String passwordValue = "ValidPass123!";

        Password password1 = Password.ofRaw(passwordValue);
        Password password2 = Password.ofRaw(passwordValue);

        assertThat(password1).hasSameHashCodeAs(password2);
    }

    @Test
    @DisplayName("Should not be equal to another password with different value")
    void shouldNotBeEqualToAnotherPasswordWithDifferentValue() {
        Password password1 = Password.ofRaw("ValidPass123!");
        Password password2 = Password.ofRaw("ValidPass456!");

        assertThat(password1).isNotEqualTo(password2);
    }

    @Test
    @DisplayName("Should return password value")
    void shouldReturnPasswordValue() {
        String expectedPassword = "ValidPass123!";

        Password password = Password.ofRaw(expectedPassword);

        assertThat(password.getValue()).isEqualTo(expectedPassword);
    }

}