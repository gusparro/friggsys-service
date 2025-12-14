package com.gusparro.friggsys.usecase.user;

import com.gusparro.friggsys.domain.entities.User;
import com.gusparro.friggsys.domain.exceptions.InvalidStateError;
import com.gusparro.friggsys.domain.repositories.UserRepositoryInterface;
import com.gusparro.friggsys.usecase.exceptions.EntityNotFoundError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeactivateUserUseCase Tests")
class DeactivateUserUseCaseTest {

    @Mock
    private UserRepositoryInterface repository;

    @InjectMocks
    private DeactivateUserUseCase useCase;

    @Mock
    private User user;

    @Mock
    private User deactivatedUser;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Should deactivate user successfully when user exists")
    void shouldDeactivateUserSuccessfullyWhenUserExists() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(user).deactivate();
        when(repository.save(user)).thenReturn(deactivatedUser);

        var result = useCase.execute(userId);

        assertNotNull(result);
        verify(repository, times(1)).findById(userId);
        verify(user, times(1)).deactivate();
        verify(repository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundError when user does not exist")
    void shouldThrowEntityNotFoundErrorWhenUserDoesNotExist() {
        when(repository.findById(userId)).thenReturn(Optional.empty());

        var exception = assertThrows(
                EntityNotFoundError.class,
                () -> useCase.execute(userId)
        );

        assertNotNull(exception);
        verify(repository, times(1)).findById(userId);
        verify(user, never()).deactivate();
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should call repository methods in correct order")
    void shouldCallRepositoryMethodsInCorrectOrder() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(user).deactivate();
        when(repository.save(user)).thenReturn(deactivatedUser);

        useCase.execute(userId);

        var inOrder = inOrder(repository, user);
        inOrder.verify(repository).findById(userId);
        inOrder.verify(user).deactivate();
        inOrder.verify(repository).save(user);
    }

    @Test
    @DisplayName("Should return UserOutput after saving deactivated user")
    void shouldReturnUserOutputAfterSavingDeactivatedUser() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(user).deactivate();
        when(repository.save(user)).thenReturn(deactivatedUser);

        var result = useCase.execute(userId);

        assertNotNull(result);
        verify(repository, times(1)).findById(userId);
        verify(user, times(1)).deactivate();
        verify(repository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should throw InvalidStateError when trying to deactivate already inactive user")
    void shouldThrowInvalidStateErrorWhenDeactivatingAlreadyInactiveUser() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        doThrow(new InvalidStateError("User", "inactive", "deactivate")).when(user).deactivate();

        var exception = assertThrows(
                InvalidStateError.class,
                () -> useCase.execute(userId)
        );

        assertNotNull(exception);
        assertEquals("It is not possible to execute 'deactivate' on User in the 'inactive' state", exception.getMessage());
        verify(repository, times(1)).findById(userId);
        verify(user, times(1)).deactivate();
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should save user exactly once per execution")
    void shouldSaveUserExactlyOncePerExecution() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(user).deactivate();
        when(repository.save(user)).thenReturn(deactivatedUser);

        useCase.execute(userId);

        verify(repository, times(1)).save(user);
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should verify user is retrieved before deactivation attempt")
    void shouldVerifyUserIsRetrievedBeforeDeactivationAttempt() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(user).deactivate();
        when(repository.save(user)).thenReturn(deactivatedUser);

        useCase.execute(userId);

        var inOrder = inOrder(repository, user);
        inOrder.verify(repository).findById(userId);
        inOrder.verify(user).deactivate();
    }

    @Test
    @DisplayName("Should not save user when EntityNotFoundError is thrown")
    void shouldNotSaveUserWhenEntityNotFoundErrorIsThrown() {
        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundError.class, () -> useCase.execute(userId));
        verify(repository, never()).save(any(User.class));
    }

}