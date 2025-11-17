package com.gusparro.friggsys.domain.repositories;

import com.gusparro.friggsys.domain.entities.User;
import com.gusparro.friggsys.domain.repositories.pagination.DomainPage;
import com.gusparro.friggsys.domain.repositories.pagination.PageParameters;
import com.gusparro.friggsys.domain.vos.Email;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryInterface {

    User save(User user);

    DomainPage<User> findAll(PageParameters parameters);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(Email email);

    boolean existsById(UUID id);

    boolean existsByEmail(Email email);

    void delete(UUID id);

}
