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

@DisplayName("Name Value Object Tests")
class NameTest {

    private static final int NAME_MIN_LENGTH = 5;
    private static final int NAME_MAX_LENGTH = 100;

    @Test
    @DisplayName("Should create a valid name")
    void shouldCreateValidName() {
        String validName = "Maria Silva";

        Name name = Name.of(validName);

        assertThat(name).isNotNull();
        assertThat(name.getValue()).isEqualTo(validName);
    }

    @Test
    @DisplayName("Should create name with min length")
    void shouldCreateNameWithMinLength() {
        String nameWithMinLength = "Carlos";

        Name name = Name.of(nameWithMinLength);

        assertThat(name).isNotNull();
        assertThat(name.getValue()).isEqualTo(nameWithMinLength);
    }

    @Test
    @DisplayName("Should create name with max length")
    void shouldCreateNameWithMaxLength() {
        String nameWithMaxLength = "A".repeat(NAME_MAX_LENGTH);

        Name name = Name.of(nameWithMaxLength);

        assertThat(name).isNotNull();
        assertThat(name.getValue()).isEqualTo(nameWithMaxLength);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "João Silva",
            "Ana Maria da Silva",
            "Pedro Henrique Oliveira Santos",
            "José Maria"
    })
    @DisplayName("Should accept valid names")
    void shouldAcceptValidNames(String validName) {
        Name name = Name.of(validName);

        assertThat(name).isNotNull();
        assertThat(name.getValue()).isEqualTo(validName);
    }

    @Test
    @DisplayName("Should throw exception when name is null")
    void shouldThrowExceptionWhenNameIsNull() {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.emptyField("name"))
                    .thenThrow(new ValidationError("Name is null or empty"));

            assertThatThrownBy(() -> Name.of(null))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @Test
    @DisplayName("Should throw exception when name is empty")
    void shouldThrowExceptionWhenNameIsEmpty() {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.emptyField("name"))
                    .thenThrow(new ValidationError("Name is null or empty"));

            assertThatThrownBy(() -> Name.of(""))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @Test
    @DisplayName("Should throw exception when name is only whitespace")
    void shouldThrowExceptionWhenNameIsOnlyWhitespace() {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.emptyField("name"))
                    .thenThrow(new ValidationError("Name is null or empty"));

            assertThatThrownBy(() -> Name.of("   "))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Ana",
            "João",
            "Jose",
            "Mar",
            "X"
    })
    @DisplayName("Should throw exception when name is shorter than min length")
    void shouldThrowExceptionWhenNameIsShorterThanMinLength(String shortName) {
        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.minLength("name", NAME_MIN_LENGTH, shortName.length()))
                    .thenThrow(new ValidationError("Name is too short"));

            assertThatThrownBy(() -> Name.of(shortName))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @Test
    @DisplayName("Should throw exception when name is longer than max length")
    void shouldThrowExceptionWhenNameIsLongerThanMaxLength() {
        String longName = "A".repeat(NAME_MAX_LENGTH + 1);

        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.maxLength("name", NAME_MAX_LENGTH, longName.length()))
                    .thenThrow(new ValidationError("Name is too long"));

            assertThatThrownBy(() -> Name.of(longName))
                    .isInstanceOf(ValidationError.class);
        }
    }

    @Test
    @DisplayName("Should be equal to another name with same value")
    void shouldBeEqualToAnotherNameWithSameValue() {
        String nameValue = "Maria Silva";

        Name name1 = Name.of(nameValue);
        Name name2 = Name.of(nameValue);

        assertThat(name1).isEqualTo(name2);
    }

    @Test
    @DisplayName("Should have same hashcode for equal names")
    void shouldHaveSameHashCodeForEqualNames() {
        String nameValue = "Maria Silva";

        Name name1 = Name.of(nameValue);
        Name name2 = Name.of(nameValue);

        assertThat(name1).hasSameHashCodeAs(name2);
    }

    @Test
    @DisplayName("Should not be equal to another name with different value")
    void shouldNotBeEqualToAnotherNameWithDifferentValue() {
        Name name1 = Name.of("Maria Silva");
        Name name2 = Name.of("João Pedro");

        assertThat(name1).isNotEqualTo(name2);
    }

    @Test
    @DisplayName("Should return name value")
    void shouldReturnNameValue() {
        String expectedName = "Maria Silva";

        Name name = Name.of(expectedName);

        assertThat(name.getValue()).isEqualTo(expectedName);
    }

}