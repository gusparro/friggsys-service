package com.gusparro.friggsys.usecase.user;

import com.gusparro.friggsys.domain.repositories.UserRepositoryInterface;
import com.gusparro.friggsys.usecase.exceptions.UseCaseExceptionFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor

@Service
public class DeleteUserUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DeleteUserUseCase.class);

    private final UserRepositoryInterface repository;

    @Transactional
    public void execute(UUID id) {
        if (!repository.existsById(id)) {
            logger.error("User with ID {} does not exists", id);

            throw  UseCaseExceptionFactory.entityNotFoundError("User",
                    "ID",
                    id.toString(),
                    "delete");
        }

        repository.delete(id);
    }

}
