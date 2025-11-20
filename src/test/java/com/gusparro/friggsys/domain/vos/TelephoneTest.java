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

@DisplayName("Telephone Value Object Tests")
class TelephoneTest {

    @Test
    @DisplayName("Should create valid telephone")
    void shouldCreateValidTelephone() {
        String validTelephone = "(11) 98765-4321";

        Telephone telephone = Telephone.of(validTelephone);

        assertThat(telephone).isNotNull();
        assertThat(telephone.getValue()).isEqualTo(validTelephone);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "(11) 98765-4321",
            "(63) 99969-4478",
    })
    @DisplayName("Should accept valid telephone formats")
    void shouldAcceptValidTelephoneFormats(String validTelephone) {
        Telephone telephone = Telephone.of(validTelephone);

        assertThat(telephone).isNotNull();
        assertThat(telephone.getValue()).isEqualTo(validTelephone);
    }

    @Test
    @DisplayName("Should throw exception when telephone is null")
    void shouldThrowExceptionWhenTelephoneIsNull() {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.emptyField("telephone"))
                    .thenThrow(new ValidationError("Telephone is null or empty"));

            assertThatThrownBy(() -> Telephone.of(null))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @Test
    @DisplayName("Should throw exception when telephone is empty")
    void shouldThrowExceptionWhenTelephoneIsEmpty() {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.emptyField("telephone"))
                    .thenThrow(new ValidationError("Telephone is null or empty"));

            assertThatThrownBy(() -> Telephone.of(""))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @Test
    @DisplayName("Should throw exception when telephone is only whitespace")
    void shouldThrowExceptionWhenTelephoneIsOnlyWhitespace() {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.emptyField("telephone"))
                    .thenThrow(new ValidationError("Telephone is null or empty"));

            assertThatThrownBy(() -> Telephone.of("   "))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "123",
            "abc",
            "abcd1234",
            "11",
            "telephone",
            "!@#$%",
            "11 9876-432",
            "11 987654321234",
            "(11) 87654321",
            "++55 11 98765-4321"
    })
    @DisplayName("Should throw exception when telephone has invalid format")
    void shouldThrowExceptionWhenTelephoneHasInvalidFormat(String invalidTelephone) {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.invalidPattern(
                    "telephone",
                    "^\\(\\d{2}\\) \\d{4,5}-\\d{4}$",
                    "Invalid telephone format"
            )).thenThrow(new ValidationError("Invalid telephone format"));

            assertThatThrownBy(() -> Telephone.of(invalidTelephone))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @Test
    @DisplayName("Should be equal to another telephone with same value")
    void shouldBeEqualToAnotherTelephoneWithSameValue() {
        String telephoneValue = "(11) 98765-4321";

        Telephone telephone1 = Telephone.of(telephoneValue);
        Telephone telephone2 = Telephone.of(telephoneValue);

        assertThat(telephone1).isEqualTo(telephone2);
    }

    @Test
    @DisplayName("Should have same hashcode for equal telephones")
    void shouldHaveSameHashCodeForEqualTelephones() {
        String telephoneValue = "(11) 98765-4321";

        Telephone telephone1 = Telephone.of(telephoneValue);
        Telephone telephone2 = Telephone.of(telephoneValue);

        assertThat(telephone1).hasSameHashCodeAs(telephone2);
    }

    @Test
    @DisplayName("Should not be equal to another telephone with different value")
    void shouldNotBeEqualToAnotherTelephoneWithDifferentValue() {
        Telephone telephone1 = Telephone.of("(11) 98765-4321");
        Telephone telephone2 = Telephone.of("(21) 98438-7800");

        assertThat(telephone1).isNotEqualTo(telephone2);
    }

    @Test
    @DisplayName("Should return telephone value")
    void shouldReturnTelephoneValue() {
        String expectedTelephone = "(11) 98765-4321";

        Telephone telephone = Telephone.of(expectedTelephone);

        assertThat(telephone.getValue()).isEqualTo(expectedTelephone);
    }

}