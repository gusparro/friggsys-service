package com.gusparro.friggsys.usecase.user;

import com.gusparro.friggsys.domain.repositories.UserRepositoryInterface;
import com.gusparro.friggsys.usecase.exceptions.UseCaseExceptionFactory;
import com.gusparro.friggsys.usecase.user.dtos.UserOutput;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor

@Service
public class DeactivateUserUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DeactivateUserUseCase.class);

    private final UserRepositoryInterface repository;

    public UserOutput execute(UUID id) {
        var user = repository.findById(id).orElseThrow(() -> {
            logger.error("User with ID {} does not exists", id);

            return UseCaseExceptionFactory.entityNotFoundError("User",
                    "ID",
                    id.toString(),
                    "deactivate");
        });

        user.deactivate();

        var updatedUser = repository.save(user);

        return UserOutput.from(updatedUser);
    }

}
