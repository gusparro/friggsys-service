package com.gusparro.friggsys.adapter.api.mappers;

import com.gusparro.friggsys.adapter.api.request.ChangePasswordRequest;
import com.gusparro.friggsys.adapter.api.request.CreateUserRequest;
import com.gusparro.friggsys.adapter.api.request.UpdateUserRequest;
import com.gusparro.friggsys.usecase.user.dtos.ChangePasswordInput;
import com.gusparro.friggsys.usecase.user.dtos.CreateUserInput;
import com.gusparro.friggsys.usecase.user.dtos.UpdateUserInput;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserApiMapper {

    public CreateUserInput toCreateUserInput(CreateUserRequest request) {
        return new CreateUserInput(
                request.name(),
                request.email(),
                request.telephone(),
                request.password()
        );
    }

    public UpdateUserInput toUpdateUserInput(UUID id, UpdateUserRequest request) {
        return new UpdateUserInput(
                id,
                request.name(),
                request.email(),
                request.telephone()
        );
    }

    public ChangePasswordInput toChangePasswordInput(UUID id, ChangePasswordRequest request) {
        return new ChangePasswordInput(
                id,
                request.currentPassword(),
                request.newPassword()
        );
    }

}
