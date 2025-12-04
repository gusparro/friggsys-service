package com.gusparro.friggsys.usecase.user;

import com.gusparro.friggsys.domain.entities.User;
import com.gusparro.friggsys.domain.repositories.UserRepositoryInterface;
import com.gusparro.friggsys.domain.vos.Email;
import com.gusparro.friggsys.domain.vos.Name;
import com.gusparro.friggsys.domain.vos.Password;
import com.gusparro.friggsys.domain.vos.Telephone;
import com.gusparro.friggsys.usecase.exceptions.UseCaseExceptionFactory;
import com.gusparro.friggsys.usecase.user.dtos.CreateUserInput;
import com.gusparro.friggsys.usecase.user.dtos.UserOutput;
import com.gusparro.friggsys.usecase.user.services.PasswordEncoderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor

@Service
public class CreateUserUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreateUserUseCase.class);

    private final UserRepositoryInterface repository;
    private final PasswordEncoderService encoder;

    @Transactional
    public UserOutput execute(CreateUserInput input) {
        var name = Name.of(input.name());
        var email = Email.of(input.email());
        var telephone = Telephone.of(input.telephone());

        if (repository.existsByEmail(email)) {
            logger.error("User with email {} already exists", email);

            throw UseCaseExceptionFactory.duplicateEmailError(email.getValue());
        }

        var rawPassword = Password.ofRaw(input.password());
        var encryptedPassword = encoder.encrypt(rawPassword);

        var user = User.create(name, email, telephone, encryptedPassword);
        var savedUser = repository.save(user);

        return UserOutput.from(savedUser);
    }

}
