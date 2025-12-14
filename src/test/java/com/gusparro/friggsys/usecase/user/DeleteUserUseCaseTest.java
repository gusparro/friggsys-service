package com.gusparro.friggsys.usecase.user;

import com.gusparro.friggsys.domain.repositories.UserRepositoryInterface;
import com.gusparro.friggsys.usecase.exceptions.EntityNotFoundError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteUserUseCase Tests")
class DeleteUserUseCaseTest {

    @Mock
    private UserRepositoryInterface repository;

    @InjectMocks
    private DeleteUserUseCase useCase;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Should delete user successfully when user exists")
    void shouldDeleteUserSuccessfullyWhenUserExists() {
        when(repository.existsById(userId)).thenReturn(true);
        doNothing().when(repository).delete(userId);

        assertDoesNotThrow(() -> useCase.execute(userId));

        verify(repository, times(1)).existsById(userId);
        verify(repository, times(1)).delete(userId);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundError when user does not exist")
    void shouldThrowEntityNotFoundErrorWhenUserDoesNotExist() {
        when(repository.existsById(userId)).thenReturn(false);

        var exception = assertThrows(
                EntityNotFoundError.class,
                () -> useCase.execute(userId)
        );

        assertNotNull(exception);
        verify(repository, times(1)).existsById(userId);
        verify(repository, never()).delete(any(UUID.class));
    }

    @Test
    @DisplayName("Should call repository methods in correct order")
    void shouldCallRepositoryMethodsInCorrectOrder() {
        when(repository.existsById(userId)).thenReturn(true);
        doNothing().when(repository).delete(userId);

        useCase.execute(userId);

        var inOrder = inOrder(repository);
        inOrder.verify(repository).existsById(userId);
        inOrder.verify(repository).delete(userId);
    }

    @Test
    @DisplayName("Should not delete user when user does not exist")
    void shouldNotDeleteUserWhenUserDoesNotExist() {
        when(repository.existsById(userId)).thenReturn(false);

        assertThrows(EntityNotFoundError.class, () -> useCase.execute(userId));
        verify(repository, never()).delete(any(UUID.class));
    }

    @Test
    @DisplayName("Should delete user exactly once per execution")
    void shouldDeleteUserExactlyOncePerExecution() {
        when(repository.existsById(userId)).thenReturn(true);
        doNothing().when(repository).delete(userId);

        useCase.execute(userId);

        verify(repository, times(1)).delete(userId);
        verify(repository, times(1)).delete(any(UUID.class));
    }

    @Test
    @DisplayName("Should check existence exactly once per execution")
    void shouldCheckExistenceExactlyOncePerExecution() {
        when(repository.existsById(userId)).thenReturn(true);
        doNothing().when(repository).delete(userId);

        useCase.execute(userId);

        verify(repository, times(1)).existsById(userId);
        verify(repository, times(1)).existsById(any(UUID.class));
    }

    @Test
    @DisplayName("Should verify user existence before deletion attempt")
    void shouldVerifyUserExistenceBeforeDeletionAttempt() {
        when(repository.existsById(userId)).thenReturn(true);
        doNothing().when(repository).delete(userId);

        useCase.execute(userId);

        var inOrder = inOrder(repository);
        inOrder.verify(repository).existsById(userId);
        inOrder.verify(repository).delete(userId);
    }

}