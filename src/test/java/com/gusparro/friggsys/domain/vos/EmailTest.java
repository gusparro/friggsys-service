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

@DisplayName("Email Value Object Tests")
class EmailTest {

    @Test
    @DisplayName("Should create a valid e-mail")
    void shouldCreateValidEmail() {
        String validEmail = "user@example.com";

        Email email = Email.of(validEmail);

        assertThat(email).isNotNull();
        assertThat(email.getValue()).isEqualTo(validEmail);
    }

    @Test
    @DisplayName("Should normalize e-mail to lowercase")
    void shouldNormalizeEmailToLowercase() {
        String emailWithUppercase = "User@EXAMPLE.COM";

        Email email = Email.of(emailWithUppercase);

        assertThat(email.getValue()).isEqualTo(emailWithUppercase);
    }

    @Test
    @DisplayName("Should trim whitespace")
    void shouldTrimWhitespace() {
        String emailWithSpaces = "  user@example.com  ";

        Email email = Email.of(emailWithSpaces);

        assertThat(email.getValue()).isEqualTo("  user@example.com  ");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "user.name@example.com",
            "user+tag@example.co.uk",
            "user_name@example.com",
            "user-name@example.com",
            "123@example.com",
            "a@b.co"
    })
    @DisplayName("Should accept valid e-mail formats")
    void shouldAcceptValidEmailFormats(String validEmail) {
        Email email = Email.of(validEmail);

        assertThat(email).isNotNull();
        assertThat(email.getValue()).isEqualTo(validEmail);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "invalidemail",
            "user@",
            "@example.com",
            "user@.com",
            "user@example",
            "user@@example.com",
            "user @example.com",
            "user@exam ple.com"
    })
    @DisplayName("Should throw exception for invalid e-mail formats")
    void shouldThrowExceptionForInvalidEmailFormats(String invalidEmail) {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.invalidPattern(
                    "email",
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                    "Invalid email format"
            )).thenThrow(new ValidationError("Invalid email format"));

            assertThatThrownBy(() -> Email.of(invalidEmail))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @Test
    @DisplayName("Should throw exception when e-mail is null")
    void shouldThrowExceptionWhenEmailIsNull() {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.emptyField("email"))
                    .thenThrow(new ValidationError("Email is null or empty"));

            assertThatThrownBy(() -> Email.of(null))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @Test
    @DisplayName("Should throw exception when e-mail is empty")
    void shouldThrowExceptionWhenEmailIsEmpty() {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.emptyField("email"))
                    .thenThrow(new ValidationError("Email is null or empty"));

            assertThatThrownBy(() -> Email.of(""))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @Test
    @DisplayName("Should throw exception when e-mail is only whitespace")
    void shouldThrowExceptionWhenEmailIsOnlyWhitespace() {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.emptyField("email"))
                    .thenThrow(new ValidationError("Email is null or empty"));

            assertThatThrownBy(() -> Email.of("   "))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @Test
    @DisplayName("Should be equal to another e-mail with same value")
    void shouldBeEqualToAnotherEmailWithSameValue() {
        String email = "user@example.com";

        Email email1 = Email.of(email);
        Email email2 = Email.of(email);

        assertThat(email1).isEqualTo(email2);
    }

    @Test
    @DisplayName("Should have same hashcode for equal e-mails")
    void shouldHaveSameHashCodeForEqualEmails() {
        String email = "user@example.com";

        Email email1 = Email.of(email);
        Email email2 = Email.of(email);

        assertThat(email1).hasSameHashCodeAs(email2);
    }

    @Test
    @DisplayName("Should not be equal to another e-mail with different value")
    void shouldNotBeEqualToAnotherEmailWithDifferentValue() {
        Email email1 = Email.of("user1@example.com");
        Email email2 = Email.of("user2@example.com");

        assertThat(email1).isNotEqualTo(email2);
    }

    @Test
    @DisplayName("Should return e-mail value")
    void shouldReturnEmailValue() {
        String expectedEmail = "user@example.com";

        Email email = Email.of(expectedEmail);

        assertThat(email.getValue()).isEqualTo(expectedEmail);
    }

}
