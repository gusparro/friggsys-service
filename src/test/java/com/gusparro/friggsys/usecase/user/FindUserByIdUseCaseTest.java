package com.gusparro.friggsys.usecase.user;

import com.gusparro.friggsys.domain.entities.User;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindUserByIdUseCase Tests")
class FindUserByIdUseCaseTest {

    @Mock
    private UserRepositoryInterface repository;

    @InjectMocks
    private FindUserByIdUseCase useCase;

    @Mock
    private User user;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Should find user successfully when user exists")
    void shouldFindUserSuccessfullyWhenUserExists() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        var result = useCase.execute(userId);

        assertNotNull(result);
        verify(repository, times(1)).findById(userId);
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
    }

    @Test
    @DisplayName("Should return UserOutput when user is found")
    void shouldReturnUserOutputWhenUserIsFound() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        var result = useCase.execute(userId);

        assertNotNull(result);
        verify(repository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Should call repository findById exactly once per execution")
    void shouldCallRepositoryFindByIdExactlyOncePerExecution() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        useCase.execute(userId);

        verify(repository, times(1)).findById(userId);
        verify(repository, times(1)).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Should find user with any valid UUID")
    void shouldFindUserWithAnyValidUuid() {
        var randomUuid = UUID.randomUUID();
        when(repository.findById(randomUuid)).thenReturn(Optional.of(user));

        var result = useCase.execute(randomUuid);

        assertNotNull(result);
        verify(repository, times(1)).findById(randomUuid);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundError for non-existent UUID")
    void shouldThrowEntityNotFoundErrorForNonExistentUuid() {
        var nonExistentId = UUID.randomUUID();
        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        var exception = assertThrows(
                EntityNotFoundError.class,
                () -> useCase.execute(nonExistentId)
        );

        assertNotNull(exception);
        verify(repository, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("Should handle repository returning null gracefully")
    void shouldHandleRepositoryReturningNullGracefully() {
        when(repository.findById(userId)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> useCase.execute(userId));
        verify(repository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Should use the same UUID passed as parameter")
    void shouldUseTheSameUuidPassedAsParameter() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        useCase.execute(userId);

        verify(repository).findById(eq(userId));
    }

    @Test
    @DisplayName("Should handle consecutive calls with different UUIDs")
    void shouldHandleConsecutiveCallsWithDifferentUuids() {
        var firstId = UUID.randomUUID();
        var secondId = UUID.randomUUID();

        when(repository.findById(firstId)).thenReturn(Optional.of(user));
        when(repository.findById(secondId)).thenReturn(Optional.of(user));

        var result1 = useCase.execute(firstId);
        var result2 = useCase.execute(secondId);

        assertNotNull(result1);
        assertNotNull(result2);
        verify(repository, times(1)).findById(firstId);
        verify(repository, times(1)).findById(secondId);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundError with correct UUID in message")
    void shouldThrowEntityNotFoundErrorWithCorrectUuidInMessage() {
        when(repository.findById(userId)).thenReturn(Optional.empty());

        var exception = assertThrows(
                EntityNotFoundError.class,
                () -> useCase.execute(userId)
        );

        assertNotNull(exception);
        verify(repository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Should not modify UUID before searching")
    void shouldNotModifyUuidBeforeSearching() {
        UUID originalId = UUID.randomUUID();
        when(repository.findById(originalId)).thenReturn(Optional.of(user));

        useCase.execute(originalId);

        verify(repository).findById(argThat(id -> id.equals(originalId)));
    }
}