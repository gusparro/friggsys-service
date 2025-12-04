package com.gusparro.friggsys.usecase.user;

import com.gusparro.friggsys.domain.repositories.UserRepositoryInterface;
import com.gusparro.friggsys.domain.vos.Email;
import com.gusparro.friggsys.usecase.exceptions.UseCaseExceptionFactory;
import com.gusparro.friggsys.usecase.user.dtos.UserOutput;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor

@Service
public class FindUserByEmailUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindUserByEmailUseCase.class);

    private final UserRepositoryInterface repository;

    public UserOutput execute(String email) {
        var emailVO = Email.of(email);

        var user = repository.findByEmail(emailVO).orElseThrow(() -> {
            logger.error("User with Email {} does not exists", email);

            return UseCaseExceptionFactory.entityNotFoundError("User",
                    "Email",
                    email,
                    "find_by_email");
        });

        return UserOutput.from(user);
    }

}
