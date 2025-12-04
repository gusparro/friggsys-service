package com.gusparro.friggsys.usecase.user;

import com.gusparro.friggsys.domain.repositories.UserRepositoryInterface;
import com.gusparro.friggsys.domain.vos.Password;
import com.gusparro.friggsys.usecase.exceptions.UseCaseExceptionFactory;
import com.gusparro.friggsys.usecase.user.dtos.ChangePasswordInput;
import com.gusparro.friggsys.usecase.user.dtos.UserOutput;
import com.gusparro.friggsys.usecase.user.services.PasswordEncoderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor

@Service
public class ChangePasswordUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ChangePasswordUseCase.class);

    private final UserRepositoryInterface repository;
    private final PasswordEncoderService encoder;

    @Transactional
    public UserOutput execute(ChangePasswordInput input) {
        var user = repository.findById(input.id()).orElseThrow(() -> {
            logger.error("User with ID {} does not exists", input.id());

            return UseCaseExceptionFactory.entityNotFoundError("User",
                    "ID",
                    input.id().toString(),
                    "change_password");
        });

        if (!encoder.matches(input.currentPassword(), user.getPassword())) {
            throw UseCaseExceptionFactory.matchingError("User",
                    "password",
                    "change_password");
        }

        var newRawPassword = Password.ofRaw(input.newPassword());
        var newEncryptedPassword = encoder.encrypt(newRawPassword);

        user.changePassword(newEncryptedPassword);

        var updatedUser = repository.save(user);

        return UserOutput.from(updatedUser);
    }

}
