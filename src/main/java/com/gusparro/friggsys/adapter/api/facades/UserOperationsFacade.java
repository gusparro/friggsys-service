package com.gusparro.friggsys.adapter.api.facades;

import com.gusparro.friggsys.adapter.api.mappers.UserApiMapper;
import com.gusparro.friggsys.adapter.api.request.ChangePasswordRequest;
import com.gusparro.friggsys.adapter.api.request.CreateUserRequest;
import com.gusparro.friggsys.adapter.api.request.UpdateUserRequest;
import com.gusparro.friggsys.adapter.api.response.UserResponse;
import com.gusparro.friggsys.adapter.exceptions.AdapterExceptionFactory;
import com.gusparro.friggsys.domain.exceptions.InvalidStateError;
import com.gusparro.friggsys.domain.exceptions.ValidationError;
import com.gusparro.friggsys.domain.repositories.pagination.PageParameters;
import com.gusparro.friggsys.usecase.exceptions.EntityNotFoundError;
import com.gusparro.friggsys.usecase.user.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor

@Component
public class UserOperationsFacade {

    private static final Logger logger = LoggerFactory.getLogger(UserOperationsFacade.class);

    private final UserApiMapper mapper;

    private final CreateUserUseCase createUserUseCase;

    private final UpdateUserUseCase updateUserUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;

    private final ActivateUserUseCase activateUserUseCase;
    private final DeactivateUserUseCase deactivateUserUseCase;
    private final BlockUserUseCase blockUserUseCase;

    private final DeleteUserUseCase deleteUserUseCase;

    private final FindUsersUseCase findUsersUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final FindUserByEmailUseCase findUserByEmailUseCase;

    public UserResponse create(CreateUserRequest request) {
        var input = mapper.toCreateUserInput(request);

        try {
            var userOutput = createUserUseCase.execute(input);

            return UserResponse.from(userOutput);
        } catch (ValidationError error) {
            logger.error(error.getMessage());

            throw AdapterExceptionFactory.invalidField("User", "create", error);
        }
    }

    public UserResponse update(UUID id, UpdateUserRequest request) {
        var input = mapper.toUpdateUserInput(id, request);

        try {
            var userOutput = updateUserUseCase.execute(input);

            return UserResponse.from(userOutput);
        } catch (EntityNotFoundError error) {
            logger.error(error.getMessage());

            throw AdapterExceptionFactory.entityNotFound("update", error);
        } catch (ValidationError error) {
            logger.error(error.getMessage());

            throw AdapterExceptionFactory.invalidField("User", "create", error);
        }
    }

    public UserResponse changePassword(UUID id, ChangePasswordRequest request) {
        var input = mapper.toChangePasswordInput(id, request);

        try {
            var userOutput = changePasswordUseCase.execute(input);

            return UserResponse.from(userOutput);
        } catch (EntityNotFoundError error) {
            logger.error(error.getMessage());

            throw AdapterExceptionFactory.entityNotFound("change_password", error);
        } catch (ValidationError error) {
            logger.error(error.getMessage());

            throw AdapterExceptionFactory.invalidField("User", "change_password", error);
        }
    }

    public UserResponse activate(UUID id) {
        try {
            var userOutput = activateUserUseCase.execute(id);

            return UserResponse.from(userOutput);
        } catch (EntityNotFoundError error) {
            logger.error(error.getMessage());

            throw AdapterExceptionFactory.entityNotFound("activate", error);
        } catch (InvalidStateError error) {
            logger.error(error.getMessage());

            throw AdapterExceptionFactory.statusConflict(error);
        }
    }

    public UserResponse deactivate(UUID id) {
        try {
            var userOutput = deactivateUserUseCase.execute(id);

            return UserResponse.from(userOutput);
        } catch (EntityNotFoundError error) {
            logger.error(error.getMessage());

            throw AdapterExceptionFactory.entityNotFound("deactivate", error);
        } catch (InvalidStateError error) {
            logger.error(error.getMessage());

            throw AdapterExceptionFactory.statusConflict(error);
        }
    }

    public UserResponse block(UUID id) {
        try {
            var userOutput = blockUserUseCase.execute(id);

            return UserResponse.from(userOutput);
        } catch (EntityNotFoundError error) {
            logger.error(error.getMessage());

            throw AdapterExceptionFactory.entityNotFound("block", error);
        } catch (InvalidStateError error) {
            logger.error(error.getMessage());

            throw AdapterExceptionFactory.statusConflict(error);
        }
    }

    public void delete(UUID id) {
        try {
            deleteUserUseCase.execute(id);
        } catch (EntityNotFoundError error) {
            logger.error(error.getMessage());

            throw AdapterExceptionFactory.entityNotFound("delete", error);
        }
    }

    public List<UserResponse> findAll(PageParameters parameters) {
        var usersOutput = findUsersUseCase.execute(parameters).getData();

        return usersOutput.stream().map(UserResponse::from).toList();
    }

    public UserResponse findById(UUID id) {
        try {
            var userOutput = findUserByIdUseCase.execute(id);

            return UserResponse.from(userOutput);
        } catch (EntityNotFoundError error) {
            logger.error(error.getMessage());

            throw AdapterExceptionFactory.resourceNotExists(error);
        } catch (ValidationError error) {
            logger.error(error.getMessage());

            throw AdapterExceptionFactory.invalidField("User", "find_by_id", error);
        }
    }

    public UserResponse findByEmail(String email) {
        try {
            var userOutput = findUserByEmailUseCase.execute(email);

            return UserResponse.from(userOutput);
        } catch (EntityNotFoundError error) {
            logger.error(error.getMessage());

            throw AdapterExceptionFactory.resourceNotExists(error);
        } catch (ValidationError error) {
            logger.error(error.getMessage());

            throw AdapterExceptionFactory.invalidField("User", "find_by_email", error);
        }
    }

}
