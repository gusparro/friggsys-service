package com.gusparro.friggsys.usecase.user;

import com.gusparro.friggsys.domain.repositories.UserRepositoryInterface;
import com.gusparro.friggsys.domain.repositories.pagination.DomainPage;
import com.gusparro.friggsys.domain.repositories.pagination.PageParameters;
import com.gusparro.friggsys.usecase.user.dtos.UserOutput;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class FindUsersUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindUsersUseCase.class);

    private final UserRepositoryInterface repository;

    public DomainPage<UserOutput> execute(PageParameters parameters) {
        var page = repository.findAll(parameters);
        var output = page.getData()
                .stream()
                .map(UserOutput::from)
                .toList();

        return DomainPage.<UserOutput>builder()
                .data(output)
                .dataAmount(page.getDataAmount())
                .pagesAmount(page.getPagesAmount())
                .pageNumber(page.getPageNumber())
                .pageSize(page.getPageSize())
                .firstPage(page.isFirstPage())
                .lastPage(page.isLastPage())
                .build();
    }

}
