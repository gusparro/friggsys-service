package com.gusparro.friggsys.usecase.user;

import com.gusparro.friggsys.domain.entities.User;
import com.gusparro.friggsys.domain.repositories.UserRepositoryInterface;
import com.gusparro.friggsys.domain.vos.Password;
import com.gusparro.friggsys.usecase.exceptions.EntityNotFoundError;
import com.gusparro.friggsys.usecase.exceptions.MatchingError;
import com.gusparro.friggsys.usecase.user.dtos.ChangePasswordInput;
import com.gusparro.friggsys.usecase.user.dtos.UserOutput;
import com.gusparro.friggsys.usecase.user.services.PasswordEncoderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChangePasswordUseCase Tests")
class ChangePasswordUseCaseTest {

    @Mock
    private UserRepositoryInterface repository;

    @Mock
    private PasswordEncoderService encoder;

    @InjectMocks
    private ChangePasswordUseCase useCase;

    @Mock
    private User user;

    @Mock
    private User updatedUser;

    private UUID userId;
    private ChangePasswordInput input;
    private static final String CURRENT_PASSWORD = "CurrentP@ss123";
    private static final String NEW_PASSWORD = "NewP@ss456";
    private static final String ENCRYPTED_CURRENT_PASSWORD = "$2a$10$encrypted_current";
    private static final String ENCRYPTED_NEW_PASSWORD = "$2a$10$encrypted_new";

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        input = new ChangePasswordInput(userId, CURRENT_PASSWORD, NEW_PASSWORD);
    }

    @Test
    @DisplayName("Should change password successfully when current password matches")
    void shouldChangePasswordSuccessfullyWhenCurrentPasswordMatches() {
        var newEncryptedPassword = Password.ofHash(ENCRYPTED_NEW_PASSWORD);

        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn(ENCRYPTED_CURRENT_PASSWORD);
        when(encoder.matches(CURRENT_PASSWORD, ENCRYPTED_CURRENT_PASSWORD)).thenReturn(true);
        when(encoder.encrypt(any(Password.class))).thenReturn(newEncryptedPassword);
        doNothing().when(user).changePassword(newEncryptedPassword);
        when(repository.save(user)).thenReturn(updatedUser);

        var result = useCase.execute(input);

        assertNotNull(result);
        verify(repository, times(1)).findById(userId);
        verify(encoder, times(1)).matches(CURRENT_PASSWORD, ENCRYPTED_CURRENT_PASSWORD);
        verify(encoder, times(1)).encrypt(any(Password.class));
        verify(user, times(1)).changePassword(newEncryptedPassword);
        verify(repository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundError when user does not exist")
    void shouldThrowEntityNotFoundErrorWhenUserDoesNotExist() {
        when(repository.findById(userId)).thenReturn(Optional.empty());

        var exception = assertThrows(
                EntityNotFoundError.class,
                () -> useCase.execute(input)
        );

        assertNotNull(exception);
        verify(repository, times(1)).findById(userId);
        verify(encoder, never()).matches(anyString(), anyString());
        verify(encoder, never()).encrypt(any(Password.class));
        verify(user, never()).changePassword(any(Password.class));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw MatchingError when current password does not match")
    void shouldThrowMatchingErrorWhenCurrentPasswordDoesNotMatch() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn(ENCRYPTED_CURRENT_PASSWORD);
        when(encoder.matches(CURRENT_PASSWORD, ENCRYPTED_CURRENT_PASSWORD)).thenReturn(false);

        var exception = assertThrows(
                MatchingError.class,
                () -> useCase.execute(input)
        );

        assertNotNull(exception);
        verify(repository, times(1)).findById(userId);
        verify(encoder, times(1)).matches(CURRENT_PASSWORD, ENCRYPTED_CURRENT_PASSWORD);
        verify(encoder, never()).encrypt(any(Password.class));
        verify(user, never()).changePassword(any(Password.class));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should call methods in correct order")
    void shouldCallMethodsInCorrectOrder() {
        var newEncryptedPassword = Password.ofHash(ENCRYPTED_NEW_PASSWORD);

        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn(ENCRYPTED_CURRENT_PASSWORD);
        when(encoder.matches(CURRENT_PASSWORD, ENCRYPTED_CURRENT_PASSWORD)).thenReturn(true);
        when(encoder.encrypt(any(Password.class))).thenReturn(newEncryptedPassword);
        doNothing().when(user).changePassword(newEncryptedPassword);
        when(repository.save(user)).thenReturn(updatedUser);

        useCase.execute(input);

        var inOrder = inOrder(repository, user, encoder);
        inOrder.verify(repository).findById(userId);
        inOrder.verify(user).getPassword();
        inOrder.verify(encoder).matches(CURRENT_PASSWORD, ENCRYPTED_CURRENT_PASSWORD);
        inOrder.verify(encoder).encrypt(any(Password.class));
        inOrder.verify(user).changePassword(newEncryptedPassword);
        inOrder.verify(repository).save(user);
    }

    @Test
    @DisplayName("Should encrypt new password before changing")
    void shouldEncryptNewPasswordBeforeChanging() {
        var newEncryptedPassword = Password.ofHash(ENCRYPTED_NEW_PASSWORD);

        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn(ENCRYPTED_CURRENT_PASSWORD);
        when(encoder.matches(CURRENT_PASSWORD, ENCRYPTED_CURRENT_PASSWORD)).thenReturn(true);
        when(encoder.encrypt(any(Password.class))).thenReturn(newEncryptedPassword);
        doNothing().when(user).changePassword(newEncryptedPassword);
        when(repository.save(user)).thenReturn(updatedUser);

        useCase.execute(input);

        verify(encoder, times(1)).encrypt(argThat(password ->
                password != null && password.getValue().equals(NEW_PASSWORD)
        ));
        verify(user, times(1)).changePassword(newEncryptedPassword);
    }

    @Test
    @DisplayName("Should verify current password before encrypting new password")
    void shouldVerifyCurrentPasswordBeforeEncryptingNewPassword() {
        var newEncryptedPassword = Password.ofHash(ENCRYPTED_NEW_PASSWORD);

        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn(ENCRYPTED_CURRENT_PASSWORD);
        when(encoder.matches(CURRENT_PASSWORD, ENCRYPTED_CURRENT_PASSWORD)).thenReturn(true);
        when(encoder.encrypt(any(Password.class))).thenReturn(newEncryptedPassword);
        doNothing().when(user).changePassword(newEncryptedPassword);
        when(repository.save(user)).thenReturn(updatedUser);

        useCase.execute(input);

        var inOrder = inOrder(encoder);
        inOrder.verify(encoder).matches(CURRENT_PASSWORD, ENCRYPTED_CURRENT_PASSWORD);
        inOrder.verify(encoder).encrypt(any(Password.class));
    }

    @Test
    @DisplayName("Should save user exactly once per execution")
    void shouldSaveUserExactlyOncePerExecution() {
        var newEncryptedPassword = Password.ofHash(ENCRYPTED_NEW_PASSWORD);

        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn(ENCRYPTED_CURRENT_PASSWORD);
        when(encoder.matches(CURRENT_PASSWORD, ENCRYPTED_CURRENT_PASSWORD)).thenReturn(true);
        when(encoder.encrypt(any(Password.class))).thenReturn(newEncryptedPassword);
        doNothing().when(user).changePassword(newEncryptedPassword);
        when(repository.save(user)).thenReturn(updatedUser);

        useCase.execute(input);

        verify(repository, times(1)).save(user);
        verify(repository, times(1)).save(any(User.class));
    }

}