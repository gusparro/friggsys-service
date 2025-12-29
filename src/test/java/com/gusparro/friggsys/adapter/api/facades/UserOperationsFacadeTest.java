package com.gusparro.friggsys.adapter.api.facades;

import com.gusparro.friggsys.adapter.api.mappers.UserApiMapper;
import com.gusparro.friggsys.adapter.api.request.ChangePasswordRequest;
import com.gusparro.friggsys.adapter.api.request.CreateUserRequest;
import com.gusparro.friggsys.adapter.api.request.UpdateUserRequest;
import com.gusparro.friggsys.adapter.exceptions.BadResquestError;
import com.gusparro.friggsys.adapter.exceptions.NotFoundError;
import com.gusparro.friggsys.domain.exceptions.InvalidStateError;
import com.gusparro.friggsys.domain.exceptions.ValidationError;
import com.gusparro.friggsys.domain.repositories.pagination.DomainPage;
import com.gusparro.friggsys.domain.repositories.pagination.PageParameters;
import com.gusparro.friggsys.usecase.exceptions.EntityNotFoundError;
import com.gusparro.friggsys.usecase.user.*;
import com.gusparro.friggsys.usecase.user.dtos.ChangePasswordInput;
import com.gusparro.friggsys.usecase.user.dtos.CreateUserInput;
import com.gusparro.friggsys.usecase.user.dtos.UpdateUserInput;
import com.gusparro.friggsys.usecase.user.dtos.UserOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserOperationsFacade Tests")
class UserOperationsFacadeTest {

    @InjectMocks
    private UserOperationsFacade facade;

    @Mock
    private UserApiMapper mapper;

    @Mock
    private CreateUserUseCase createUserUseCase;

    @Mock
    private UpdateUserUseCase updateUserUseCase;

    @Mock
    private ChangePasswordUseCase changePasswordUseCase;

    @Mock
    private ActivateUserUseCase activateUserUseCase;

    @Mock
    private DeactivateUserUseCase deactivateUserUseCase;

    @Mock
    private BlockUserUseCase blockUserUseCase;

    @Mock
    private DeleteUserUseCase deleteUserUseCase;

    @Mock
    private FindUsersUseCase findUsersUseCase;

    @Mock
    private FindUserByIdUseCase findUserByIdUseCase;

    @Mock
    private FindUserByEmailUseCase findUserByEmailUseCase;

    @Mock
    private CreateUserRequest createUserRequest;

    @Mock
    private UpdateUserRequest updateUserRequest;

    @Mock
    private ChangePasswordRequest changePasswordRequest;

    @Mock
    private CreateUserInput createUserInput;

    @Mock
    private UpdateUserInput updateUserInput;

    @Mock
    private ChangePasswordInput changePasswordInput;

    @Mock
    private UserOutput userOutput;

    @BeforeEach
    void setUp() {
        // Configurando o mock de UserOutput para retornar valores válidos
        lenient().when(userOutput.status()).thenReturn(com.gusparro.friggsys.domain.enums.UserStatus.ACTIVE);
        lenient().when(userOutput.id()).thenReturn(UUID.randomUUID());
        lenient().when(userOutput.name()).thenReturn("Test User");
        lenient().when(userOutput.email()).thenReturn("test@example.com");
        lenient().when(userOutput.telephone()).thenReturn("(11) 99999-9999");
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        when(mapper.toCreateUserInput(createUserRequest)).thenReturn(createUserInput);
        when(createUserUseCase.execute(createUserInput)).thenReturn(userOutput);

        var result = facade.create(createUserRequest);

        assertNotNull(result);
        verify(mapper).toCreateUserInput(createUserRequest);
        verify(createUserUseCase).execute(createUserInput);
    }

    @Test
    @DisplayName("Should throw BadRequestError when create fails with ValidationError")
    void shouldThrowBadRequestErrorWhenCreateFailsWithValidationError() {
        Map<String, Object> details = Map.of("field", "email");
        var validationError = new ValidationError("Invalid email", "email", details);

        when(mapper.toCreateUserInput(createUserRequest)).thenReturn(createUserInput);
        when(createUserUseCase.execute(createUserInput)).thenThrow(validationError);

        assertThrows(BadResquestError.class, () -> facade.create(createUserRequest));
        verify(createUserUseCase).execute(createUserInput);
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        var userId = UUID.randomUUID();

        when(mapper.toUpdateUserInput(userId, updateUserRequest)).thenReturn(updateUserInput);
        when(updateUserUseCase.execute(updateUserInput)).thenReturn(userOutput);

        var result = facade.update(userId, updateUserRequest);

        assertNotNull(result);
        verify(mapper).toUpdateUserInput(userId, updateUserRequest);
        verify(updateUserUseCase).execute(updateUserInput);
    }

    @Test
    @DisplayName("Should throw BadRequestError when update fails with EntityNotFoundError")
    void shouldThrowBadRequestErrorWhenUpdateFailsWithEntityNotFoundError() {
        var userId = UUID.randomUUID();
        Map<String, Object> details = Map.of("id", userId.toString());
        var entityNotFoundError = new EntityNotFoundError("User", "ID", userId.toString(), details);

        when(mapper.toUpdateUserInput(userId, updateUserRequest)).thenReturn(updateUserInput);
        when(updateUserUseCase.execute(updateUserInput)).thenThrow(entityNotFoundError);

        assertThrows(BadResquestError.class, () -> facade.update(userId, updateUserRequest));
        verify(updateUserUseCase).execute(updateUserInput);
    }

    @Test
    @DisplayName("Should throw BadRequestError when update fails with ValidationError")
    void shouldThrowBadRequestErrorWhenUpdateFailsWithValidationError() {
        var userId = UUID.randomUUID();
        Map<String, Object> details = Map.of("field", "name");
        var validationError = new ValidationError("Invalid name", "name", details);

        when(mapper.toUpdateUserInput(userId, updateUserRequest)).thenReturn(updateUserInput);
        when(updateUserUseCase.execute(updateUserInput)).thenThrow(validationError);

        assertThrows(BadResquestError.class, () -> facade.update(userId, updateUserRequest));
        verify(updateUserUseCase).execute(updateUserInput);
    }

    @Test
    @DisplayName("Should change password successfully")
    void shouldChangePasswordSuccessfully() {
        var userId = UUID.randomUUID();

        when(mapper.toChangePasswordInput(userId, changePasswordRequest)).thenReturn(changePasswordInput);
        when(changePasswordUseCase.execute(changePasswordInput)).thenReturn(userOutput);

        var result = facade.changePassword(userId, changePasswordRequest);

        assertNotNull(result);
        verify(mapper).toChangePasswordInput(userId, changePasswordRequest);
        verify(changePasswordUseCase).execute(changePasswordInput);
    }

    @Test
    @DisplayName("Should throw BadRequestError when change password fails with EntityNotFoundError")
    void shouldThrowBadRequestErrorWhenChangePasswordFailsWithEntityNotFoundError() {
        var userId = UUID.randomUUID();
        Map<String, Object> details = Map.of("id", userId.toString());
        var entityNotFoundError = new EntityNotFoundError("User", "ID", userId.toString(), details);

        when(mapper.toChangePasswordInput(userId, changePasswordRequest)).thenReturn(changePasswordInput);
        when(changePasswordUseCase.execute(changePasswordInput)).thenThrow(entityNotFoundError);

        assertThrows(BadResquestError.class, () -> facade.changePassword(userId, changePasswordRequest));
        verify(changePasswordUseCase).execute(changePasswordInput);
    }

    @Test
    @DisplayName("Should throw BadRequestError when change password fails with ValidationError")
    void shouldThrowBadRequestErrorWhenChangePasswordFailsWithValidationError() {
        var userId = UUID.randomUUID();
        Map<String, Object> details = Map.of("field", "password");
        var validationError = new ValidationError("Password too short", "password", details);

        when(mapper.toChangePasswordInput(userId, changePasswordRequest)).thenReturn(changePasswordInput);
        when(changePasswordUseCase.execute(changePasswordInput)).thenThrow(validationError);

        assertThrows(BadResquestError.class, () -> facade.changePassword(userId, changePasswordRequest));
        verify(changePasswordUseCase).execute(changePasswordInput);
    }

    @Test
    @DisplayName("Should activate user successfully")
    void shouldActivateUserSuccessfully() {
        var userId = UUID.randomUUID();

        when(activateUserUseCase.execute(userId)).thenReturn(userOutput);

        var result = facade.activate(userId);

        assertNotNull(result);
        verify(activateUserUseCase).execute(userId);
    }

    @Test
    @DisplayName("Should throw BadRequestError when activate fails with EntityNotFoundError")
    void shouldThrowBadRequestErrorWhenActivateFailsWithEntityNotFoundError() {
        var userId = UUID.randomUUID();
        Map<String, Object> details = Map.of("id", userId.toString());
        var entityNotFoundError = new EntityNotFoundError("User", "ID", userId.toString(), details);

        when(activateUserUseCase.execute(userId)).thenThrow(entityNotFoundError);

        assertThrows(BadResquestError.class, () -> facade.activate(userId));
        verify(activateUserUseCase).execute(userId);
    }

    @Test
    @DisplayName("Should throw BadRequestError when activate fails with InvalidStateError")
    void shouldThrowBadRequestErrorWhenActivateFailsWithInvalidStateError() {
        var userId = UUID.randomUUID();
        Map<String, Object> details = Map.of("currentStatus", "BLOCKED");
        var invalidStateError = new InvalidStateError("User", "BLOCKED", "activate", details);

        when(activateUserUseCase.execute(userId)).thenThrow(invalidStateError);

        assertThrows(BadResquestError.class, () -> facade.activate(userId));
        verify(activateUserUseCase).execute(userId);
    }

    @Test
    @DisplayName("Should deactivate user successfully")
    void shouldDeactivateUserSuccessfully() {
        var userId = UUID.randomUUID();

        when(deactivateUserUseCase.execute(userId)).thenReturn(userOutput);

        var result = facade.deactivate(userId);

        assertNotNull(result);
        verify(deactivateUserUseCase).execute(userId);
    }

    @Test
    @DisplayName("Should throw BadRequestError when deactivate fails with EntityNotFoundError")
    void shouldThrowBadRequestErrorWhenDeactivateFailsWithEntityNotFoundError() {
        var userId = UUID.randomUUID();
        Map<String, Object> details = Map.of("id", userId.toString());
        var entityNotFoundError = new EntityNotFoundError("User", "ID", userId.toString(), details);

        when(deactivateUserUseCase.execute(userId)).thenThrow(entityNotFoundError);

        assertThrows(BadResquestError.class, () -> facade.deactivate(userId));
        verify(deactivateUserUseCase).execute(userId);
    }

    @Test
    @DisplayName("Should throw BadRequestError when deactivate fails with InvalidStateError")
    void shouldThrowBadRequestErrorWhenDeactivateFailsWithInvalidStateError() {
        var userId = UUID.randomUUID();
        Map<String, Object> details = Map.of("currentStatus", "INACTIVE");
        var invalidStateError = new InvalidStateError("User", "INACTIVE", "deactivate", details);

        when(deactivateUserUseCase.execute(userId)).thenThrow(invalidStateError);

        assertThrows(BadResquestError.class, () -> facade.deactivate(userId));
        verify(deactivateUserUseCase).execute(userId);
    }

    @Test
    @DisplayName("Should block user successfully")
    void shouldBlockUserSuccessfully() {
        var userId = UUID.randomUUID();

        when(blockUserUseCase.execute(userId)).thenReturn(userOutput);

        var result = facade.block(userId);

        assertNotNull(result);
        verify(blockUserUseCase).execute(userId);
    }

    @Test
    @DisplayName("Should throw BadRequestError when block fails with EntityNotFoundError")
    void shouldThrowBadRequestErrorWhenBlockFailsWithEntityNotFoundError() {
        var userId = UUID.randomUUID();
        Map<String, Object> details = Map.of("id", userId.toString());
        var entityNotFoundError = new EntityNotFoundError("User", "ID", userId.toString(), details);

        when(blockUserUseCase.execute(userId)).thenThrow(entityNotFoundError);

        assertThrows(BadResquestError.class, () -> facade.block(userId));
        verify(blockUserUseCase).execute(userId);
    }

    @Test
    @DisplayName("Should throw BadRequestError when block fails with InvalidStateError")
    void shouldThrowBadRequestErrorWhenBlockFailsWithInvalidStateError() {
        var userId = UUID.randomUUID();
        Map<String, Object> details = Map.of("currentStatus", "BLOCKED");
        var invalidStateError = new InvalidStateError("User", "BLOCKED", "block", details);

        when(blockUserUseCase.execute(userId)).thenThrow(invalidStateError);

        assertThrows(BadResquestError.class, () -> facade.block(userId));
        verify(blockUserUseCase).execute(userId);
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        var userId = UUID.randomUUID();

        facade.delete(userId);

        verify(deleteUserUseCase).execute(userId);
    }

    @Test
    @DisplayName("Should throw BadRequestError when delete fails with EntityNotFoundError")
    void shouldThrowBadRequestErrorWhenDeleteFailsWithEntityNotFoundError() {
        var userId = UUID.randomUUID();
        Map<String, Object> details = Map.of("id", userId.toString());
        var entityNotFoundError = new EntityNotFoundError("User", "ID", userId.toString(), details);

        doThrow(entityNotFoundError).when(deleteUserUseCase).execute(userId);

        assertThrows(BadResquestError.class, () -> facade.delete(userId));
        verify(deleteUserUseCase).execute(userId);
    }

    @Test
    @DisplayName("Should find all users successfully")
    void shouldFindAllUsersSuccessfully() {
        var parameters = PageParameters.builder()
                .page(0)
                .size(10)
                .build();

        var domainPage = DomainPage.<UserOutput>builder()
                .data(List.of(userOutput, userOutput))
                .dataAmount(2L)
                .pagesAmount(1)
                .pageNumber(0)
                .pageSize(10)
                .firstPage(true)
                .lastPage(true)
                .build();

        when(findUsersUseCase.execute(parameters)).thenReturn(domainPage);

        var result = facade.findAll(parameters);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(findUsersUseCase).execute(parameters);
    }

    @Test
    @DisplayName("Should find all users returning empty list")
    void shouldFindAllUsersReturningEmptyList() {
        var parameters = PageParameters.builder()
                .page(0)
                .size(10)
                .build();

        var emptyPage = DomainPage.<UserOutput>builder()
                .data(List.of())
                .dataAmount(0L)
                .pagesAmount(0)
                .pageNumber(0)
                .pageSize(10)
                .firstPage(true)
                .lastPage(true)
                .build();

        when(findUsersUseCase.execute(parameters)).thenReturn(emptyPage);

        var result = facade.findAll(parameters);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(findUsersUseCase).execute(parameters);
    }

    @Test
    @DisplayName("Should find user by id successfully")
    void shouldFindUserByIdSuccessfully() {
        var userId = UUID.randomUUID();

        when(findUserByIdUseCase.execute(userId)).thenReturn(userOutput);

        var result = facade.findById(userId);

        assertNotNull(result);
        verify(findUserByIdUseCase).execute(userId);
    }

    @Test
    @DisplayName("Should throw NotFoundError when find by id fails with EntityNotFoundError")
    void shouldThrowNotFoundErrorWhenFindByIdFailsWithEntityNotFoundError() {
        var userId = UUID.randomUUID();
        Map<String, Object> details = Map.of("id", userId.toString());
        var entityNotFoundError = new EntityNotFoundError("User", "ID", userId.toString(), details);

        when(findUserByIdUseCase.execute(userId)).thenThrow(entityNotFoundError);

        assertThrows(NotFoundError.class, () -> facade.findById(userId));
        verify(findUserByIdUseCase).execute(userId);
    }

    @Test
    @DisplayName("Should throw BadRequestError when find by id fails with ValidationError")
    void shouldThrowBadRequestErrorWhenFindByIdFailsWithValidationError() {
        var userId = UUID.randomUUID();
        Map<String, Object> details = Map.of("field", "id");
        var validationError = new ValidationError("Invalid ID", "id", details);

        when(findUserByIdUseCase.execute(userId)).thenThrow(validationError);

        assertThrows(BadResquestError.class, () -> facade.findById(userId));
        verify(findUserByIdUseCase).execute(userId);
    }

    @Test
    @DisplayName("Should find user by email successfully")
    void shouldFindUserByEmailSuccessfully() {
        var email = "user@example.com";

        when(findUserByEmailUseCase.execute(email)).thenReturn(userOutput);

        var result = facade.findByEmail(email);

        assertNotNull(result);
        verify(findUserByEmailUseCase).execute(email);
    }

    @Test
    @DisplayName("Should throw NotFoundError when find by email fails with EntityNotFoundError")
    void shouldThrowNotFoundErrorWhenFindByEmailFailsWithEntityNotFoundError() {
        var email = "notfound@example.com";
        Map<String, Object> details = Map.of("email", email);
        var entityNotFoundError = new EntityNotFoundError("User", "Email", email, details);

        when(findUserByEmailUseCase.execute(email)).thenThrow(entityNotFoundError);

        assertThrows(NotFoundError.class, () -> facade.findByEmail(email));
        verify(findUserByEmailUseCase).execute(email);
    }

    @Test
    @DisplayName("Should throw BadRequestError when find by email fails with ValidationError")
    void shouldThrowBadRequestErrorWhenFindByEmailFailsWithValidationError() {
        var email = "invalid-email";
        Map<String, Object> details = Map.of("field", "email");
        var validationError = new ValidationError("Invalid email format", "email", details);

        when(findUserByEmailUseCase.execute(email)).thenThrow(validationError);

        assertThrows(BadResquestError.class, () -> facade.findByEmail(email));
        verify(findUserByEmailUseCase).execute(email);
    }

    @Test
    @DisplayName("Should handle multiple create operations")
    void shouldHandleMultipleCreateOperations() {
        var request1 = mock(CreateUserRequest.class);
        var request2 = mock(CreateUserRequest.class);
        var input1 = mock(CreateUserInput.class);
        var input2 = mock(CreateUserInput.class);
        var output1 = mock(UserOutput.class);
        var output2 = mock(UserOutput.class);

        when(output1.status()).thenReturn(com.gusparro.friggsys.domain.enums.UserStatus.ACTIVE);
        when(output2.status()).thenReturn(com.gusparro.friggsys.domain.enums.UserStatus.ACTIVE);

        when(mapper.toCreateUserInput(request1)).thenReturn(input1);
        when(mapper.toCreateUserInput(request2)).thenReturn(input2);
        when(createUserUseCase.execute(input1)).thenReturn(output1);
        when(createUserUseCase.execute(input2)).thenReturn(output2);

        var result1 = facade.create(request1);
        var result2 = facade.create(request2);

        assertNotNull(result1);
        assertNotNull(result2);
        verify(createUserUseCase).execute(input1);
        verify(createUserUseCase).execute(input2);
    }

    @Test
    @DisplayName("Should handle multiple update operations for different users")
    void shouldHandleMultipleUpdateOperationsForDifferentUsers() {
        var userId1 = UUID.randomUUID();
        var userId2 = UUID.randomUUID();

        when(mapper.toUpdateUserInput(any(UUID.class), any(UpdateUserRequest.class))).thenReturn(updateUserInput);
        when(updateUserUseCase.execute(updateUserInput)).thenReturn(userOutput);

        facade.update(userId1, updateUserRequest);
        facade.update(userId2, updateUserRequest);

        verify(updateUserUseCase, times(2)).execute(updateUserInput);
    }

    @Test
    @DisplayName("Should handle activate, deactivate, and block operations for same user")
    void shouldHandleActivateDeactivateAndBlockOperationsForSameUser() {
        var userId = UUID.randomUUID();

        when(activateUserUseCase.execute(userId)).thenReturn(userOutput);
        when(deactivateUserUseCase.execute(userId)).thenReturn(userOutput);
        when(blockUserUseCase.execute(userId)).thenReturn(userOutput);

        facade.activate(userId);
        facade.deactivate(userId);
        facade.block(userId);

        verify(activateUserUseCase).execute(userId);
        verify(deactivateUserUseCase).execute(userId);
        verify(blockUserUseCase).execute(userId);
    }

    @Test
    @DisplayName("Should handle find operations with different parameters")
    void shouldHandleFindOperationsWithDifferentParameters() {
        var params1 = PageParameters.builder().page(0).size(10).build();
        var params2 = PageParameters.builder().page(1).size(20).build();

        var page1 = DomainPage.<UserOutput>builder()
                .data(List.of(userOutput))
                .dataAmount(1L)
                .pagesAmount(1)
                .pageNumber(0)
                .pageSize(10)
                .firstPage(true)
                .lastPage(true)
                .build();

        var page2 = DomainPage.<UserOutput>builder()
                .data(List.of(userOutput, userOutput))
                .dataAmount(2L)
                .pagesAmount(1)
                .pageNumber(1)
                .pageSize(20)
                .firstPage(false)
                .lastPage(true)
                .build();

        when(findUsersUseCase.execute(params1)).thenReturn(page1);
        when(findUsersUseCase.execute(params2)).thenReturn(page2);

        var result1 = facade.findAll(params1);
        var result2 = facade.findAll(params2);

        assertEquals(1, result1.size());
        assertEquals(2, result2.size());
    }

    @Test
    @DisplayName("Should handle delete operations for multiple users")
    void shouldHandleDeleteOperationsForMultipleUsers() {
        var userId1 = UUID.randomUUID();
        var userId2 = UUID.randomUUID();
        var userId3 = UUID.randomUUID();

        facade.delete(userId1);
        facade.delete(userId2);
        facade.delete(userId3);

        verify(deleteUserUseCase).execute(userId1);
        verify(deleteUserUseCase).execute(userId2);
        verify(deleteUserUseCase).execute(userId3);
    }

    @Test
    @DisplayName("Should handle password change for multiple users")
    void shouldHandlePasswordChangeForMultipleUsers() {
        var userId1 = UUID.randomUUID();
        var userId2 = UUID.randomUUID();

        when(mapper.toChangePasswordInput(any(UUID.class), any(ChangePasswordRequest.class)))
                .thenReturn(changePasswordInput);
        when(changePasswordUseCase.execute(changePasswordInput)).thenReturn(userOutput);

        facade.changePassword(userId1, changePasswordRequest);
        facade.changePassword(userId2, changePasswordRequest);

        verify(changePasswordUseCase, times(2)).execute(changePasswordInput);
    }

    @Test
    @DisplayName("Should find users by different emails")
    void shouldFindUsersByDifferentEmails() {
        var email1 = "user1@example.com";
        var email2 = "user2@example.com";
        var output1 = mock(UserOutput.class);
        var output2 = mock(UserOutput.class);

        when(output1.status()).thenReturn(com.gusparro.friggsys.domain.enums.UserStatus.ACTIVE);
        when(output2.status()).thenReturn(com.gusparro.friggsys.domain.enums.UserStatus.ACTIVE);

        when(findUserByEmailUseCase.execute(email1)).thenReturn(output1);
        when(findUserByEmailUseCase.execute(email2)).thenReturn(output2);

        facade.findByEmail(email1);
        facade.findByEmail(email2);

        verify(findUserByEmailUseCase).execute(email1);
        verify(findUserByEmailUseCase).execute(email2);
    }

    @Test
    @DisplayName("Should find users by different ids")
    void shouldFindUsersByDifferentIds() {
        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();
        var output1 = mock(UserOutput.class);
        var output2 = mock(UserOutput.class);

        when(output1.status()).thenReturn(com.gusparro.friggsys.domain.enums.UserStatus.ACTIVE);
        when(output2.status()).thenReturn(com.gusparro.friggsys.domain.enums.UserStatus.ACTIVE);

        when(findUserByIdUseCase.execute(id1)).thenReturn(output1);
        when(findUserByIdUseCase.execute(id2)).thenReturn(output2);

        facade.findById(id1);
        facade.findById(id2);

        verify(findUserByIdUseCase).execute(id1);
        verify(findUserByIdUseCase).execute(id2);
    }

    @Test
    @DisplayName("Should propagate all exceptions correctly from create")
    void shouldPropagateAllExceptionsCorrectlyFromCreate() {
        Map<String, Object> details = Map.of("field", "name");
        var validationError = new ValidationError("Name is required", "name", details);

        when(mapper.toCreateUserInput(createUserRequest)).thenReturn(createUserInput);
        when(createUserUseCase.execute(createUserInput)).thenThrow(validationError);

        var exception = assertThrows(BadResquestError.class, () -> facade.create(createUserRequest));
        assertNotNull(exception);
    }

    @Test
    @DisplayName("Should propagate all exceptions correctly from update")
    void shouldPropagateAllExceptionsCorrectlyFromUpdate() {
        var userId = UUID.randomUUID();
        Map<String, Object> details = Map.of("id", userId.toString());
        var entityNotFoundError = new EntityNotFoundError("User", "ID", userId.toString(), details);

        when(mapper.toUpdateUserInput(userId, updateUserRequest)).thenReturn(updateUserInput);
        when(updateUserUseCase.execute(updateUserInput)).thenThrow(entityNotFoundError);

        var exception = assertThrows(BadResquestError.class, () -> facade.update(userId, updateUserRequest));
        assertNotNull(exception);
    }

    @Test
    @DisplayName("Should handle large page of users")
    void shouldHandleLargePageOfUsers() {
        var parameters = PageParameters.builder()
                .page(0)
                .size(100)
                .build();

        var largeList = List.of(
                userOutput, userOutput, userOutput, userOutput, userOutput,
                userOutput, userOutput, userOutput, userOutput, userOutput
        );

        var domainPage = DomainPage.<UserOutput>builder()
                .data(largeList)
                .dataAmount(10L)
                .pagesAmount(1)
                .pageNumber(0)
                .pageSize(100)
                .firstPage(true)
                .lastPage(true)
                .build();

        when(findUsersUseCase.execute(parameters)).thenReturn(domainPage);

        var result = facade.findAll(parameters);

        assertEquals(10, result.size());
        verify(findUsersUseCase).execute(parameters);
    }

    @Test
    @DisplayName("Should verify mapper is called with correct parameters")
    void shouldVerifyMapperIsCalledWithCorrectParameters() {
        var userId = UUID.randomUUID();

        when(mapper.toUpdateUserInput(userId, updateUserRequest)).thenReturn(updateUserInput);
        when(updateUserUseCase.execute(updateUserInput)).thenReturn(userOutput);

        facade.update(userId, updateUserRequest);

        verify(mapper).toUpdateUserInput(userId, updateUserRequest);
    }

    @Test
    @DisplayName("Should verify all use cases are called once per operation")
    void shouldVerifyAllUseCasesAreCalledOncePerOperation() {
        var userId = UUID.randomUUID();

        when(activateUserUseCase.execute(userId)).thenReturn(userOutput);
        when(findUserByIdUseCase.execute(userId)).thenReturn(userOutput);

        facade.activate(userId);
        facade.findById(userId);

        verify(activateUserUseCase, times(1)).execute(userId);
        verify(findUserByIdUseCase, times(1)).execute(userId);
    }

}