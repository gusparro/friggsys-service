package com.gusparro.friggsys.adapter.persistence.repositories;

import com.gusparro.friggsys.adapter.persistence.mappers.UserPersistenceMapper;
import com.gusparro.friggsys.domain.entities.User;
import com.gusparro.friggsys.domain.repositories.UserRepositoryInterface;
import com.gusparro.friggsys.domain.repositories.pagination.DomainPage;
import com.gusparro.friggsys.domain.repositories.pagination.PageParameters;
import com.gusparro.friggsys.domain.vos.Email;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class UserRepositoryAdapter implements UserRepositoryInterface {

    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryAdapter.class);

    private final UserJpaRepository repository;
    private final UserPersistenceMapper mapper;

    @Override
    public User save(User user) {
        var entity = mapper.toEntity(user);
        var savedEntity = repository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public DomainPage<User> findAll(PageParameters parameters) {
        var pageable = createPageable(parameters);
        var page = repository.findAll(pageable);

        var users = page.getContent()
                .stream()
                .map(mapper::toDomain)
                .toList();

        return DomainPage.<User>builder()
                .data(users)
                .dataAmount(page.getTotalElements())
                .pagesAmount(page.getTotalPages())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .firstPage(page.isFirst())
                .lastPage(page.isLast())
                .build();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return repository.findByEmail(email.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return repository.existsByEmail(email.getValue());
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    private Pageable createPageable(PageParameters parameters) {
        if (parameters.getOrderBy() != null && !parameters.getOrderBy().isBlank()) {
            Sort.Direction direction = Sort.Direction.valueOf(parameters.getDirection().name());
            Sort sort = Sort.by(direction, parameters.getOrderBy());
            return PageRequest.of(parameters.getPage(), parameters.getSize(), sort);
        }

        return PageRequest.of(parameters.getPage(), parameters.getSize());
    }
}