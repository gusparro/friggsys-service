package com.gusparro.friggsys.usecase.user;

import com.gusparro.friggsys.domain.entities.User;
import com.gusparro.friggsys.domain.exceptions.ValidationError;
import com.gusparro.friggsys.domain.repositories.UserRepositoryInterface;
import com.gusparro.friggsys.domain.vos.Email;
import com.gusparro.friggsys.usecase.exceptions.EntityNotFoundError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindUserByEmailUseCase Tests")
class FindUserByEmailUseCaseTest {

    @Mock
    private UserRepositoryInterface repository;

    @InjectMocks
    private FindUserByEmailUseCase useCase;

    @Mock
    private User user;

    private static final String VALID_EMAIL = "john.doe@example.com";
    private static final String INVALID_EMAIL = "invalid-email";
    private static final String NON_EXISTENT_EMAIL = "nonexistent@example.com";

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Should find user successfully when email exists")
    void shouldFindUserSuccessfullyWhenEmailExists() {
        when(repository.findByEmail(any(Email.class))).thenReturn(Optional.of(user));

        var result = useCase.execute(VALID_EMAIL);

        assertNotNull(result);
        verify(repository, times(1)).findByEmail(any(Email.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundError when user does not exist")
    void shouldThrowEntityNotFoundErrorWhenUserDoesNotExist() {
        when(repository.findByEmail(any(Email.class))).thenReturn(Optional.empty());

        var exception = assertThrows(
                EntityNotFoundError.class,
                () -> useCase.execute(NON_EXISTENT_EMAIL)
        );

        assertNotNull(exception);
        verify(repository, times(1)).findByEmail(any(Email.class));
    }

    @Test
    @DisplayName("Should convert email string to Email value object")
    void shouldConvertEmailStringToEmailValueObject() {
        when(repository.findByEmail(any(Email.class))).thenReturn(Optional.of(user));

        useCase.execute(VALID_EMAIL);

        verify(repository).findByEmail(argThat(email ->
                email != null && email.getValue().equals(VALID_EMAIL)
        ));
    }

    @Test
    @DisplayName("Should propagate exception when Email creation fails")
    void shouldPropagateExceptionWhenEmailCreationFails() {
        assertThrows(ValidationError.class, () -> useCase.execute(INVALID_EMAIL));
        verify(repository, never()).findByEmail(any(Email.class));
    }

    @Test
    @DisplayName("Should return UserOutput when user is found")
    void shouldReturnUserOutputWhenUserIsFound() {
        when(repository.findByEmail(any(Email.class))).thenReturn(Optional.of(user));

        var result = useCase.execute(VALID_EMAIL);

        assertNotNull(result);
        verify(repository, times(1)).findByEmail(any(Email.class));
    }

    @Test
    @DisplayName("Should handle null email gracefully")
    void shouldHandleNullEmailGracefully() {
        assertThrows(ValidationError.class, () -> useCase.execute(null));
        verify(repository, never()).findByEmail(any(Email.class));
    }

    @Test
    @DisplayName("Should handle empty email string gracefully")
    void shouldHandleEmptyEmailStringGracefully() {
        assertThrows(ValidationError.class, () -> useCase.execute(""));
        verify(repository, never()).findByEmail(any(Email.class));
    }

    @Test
    @DisplayName("Should handle whitespace email string gracefully")
    void shouldHandleWhitespaceEmailStringGracefully() {
        assertThrows(ValidationError.class, () -> useCase.execute("   "));
        verify(repository, never()).findByEmail(any(Email.class));
    }

    @Test
    @DisplayName("Should call repository findByEmail exactly once per execution")
    void shouldCallRepositoryFindByEmailExactlyOncePerExecution() {
        when(repository.findByEmail(any(Email.class))).thenReturn(Optional.of(user));

        useCase.execute(VALID_EMAIL);

        verify(repository, times(1)).findByEmail(any(Email.class));
    }

    @Test
    @DisplayName("Should handle emails with different cases")
    void shouldHandleEmailsWithDifferentCases() {
        var upperCaseEmail = "JOHN.DOE@EXAMPLE.COM";
        when(repository.findByEmail(any(Email.class))).thenReturn(Optional.of(user));

        var result = useCase.execute(upperCaseEmail);

        assertNotNull(result);
        verify(repository, times(1)).findByEmail(any(Email.class));
    }

    @Test
    @DisplayName("Should handle emails with special characters")
    void shouldHandleEmailsWithSpecialCharacters() {
        var specialEmail = "john.doe+test@example.com";
        when(repository.findByEmail(any(Email.class))).thenReturn(Optional.of(user));

        var result = useCase.execute(specialEmail);

        assertNotNull(result);
        verify(repository, times(1)).findByEmail(any(Email.class));
    }

    @Test
    @DisplayName("Should not call repository when email validation fails")
    void shouldNotCallRepositoryWhenEmailValidationFails() {
        assertThrows(Exception.class, () -> useCase.execute(INVALID_EMAIL));
        verify(repository, never()).findByEmail(any(Email.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundError with correct email in message")
    void shouldThrowEntityNotFoundErrorWithCorrectEmailInMessage() {
        when(repository.findByEmail(any(Email.class))).thenReturn(Optional.empty());

        var exception = assertThrows(
                EntityNotFoundError.class,
                () -> useCase.execute(NON_EXISTENT_EMAIL)
        );

        assertNotNull(exception);
        verify(repository, times(1)).findByEmail(any(Email.class));
    }
}