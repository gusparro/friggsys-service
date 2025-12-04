package com.gusparro.friggsys.usecase.user;

import com.gusparro.friggsys.domain.repositories.UserRepositoryInterface;
import com.gusparro.friggsys.domain.vos.Email;
import com.gusparro.friggsys.domain.vos.Name;
import com.gusparro.friggsys.domain.vos.Telephone;
import com.gusparro.friggsys.usecase.exceptions.UseCaseExceptionFactory;
import com.gusparro.friggsys.usecase.user.dtos.UpdateUserInput;
import com.gusparro.friggsys.usecase.user.dtos.UserOutput;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor

@Service
public class UpdateUserUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UpdateUserUseCase.class);

    private final UserRepositoryInterface repository;

    @Transactional
    public UserOutput execute(UpdateUserInput input) {
        var user = repository.findById(input.id()).orElseThrow(() -> {
            logger.error("User with ID {} does not exists", input.id());

            return UseCaseExceptionFactory.entityNotFoundError("User", "ID", input.id().toString(), "update");
        });

        var name = Name.of(input.name());
        var email = Email.of(input.email());
        var telephone = Telephone.of(input.telephone());

        if (repository.existsByEmail(email)) {
            logger.error("User with email {} already exists", email);

            throw UseCaseExceptionFactory.duplicateEmailError(email.getValue());
        }

        user.update(name, email, telephone);

        var updatedUser = repository.save(user);

        return UserOutput.from(updatedUser);
    }

}
