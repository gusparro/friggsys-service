package com.gusparro.friggsys.usecase.user;

import com.gusparro.friggsys.domain.entities.User;
import com.gusparro.friggsys.domain.repositories.UserRepositoryInterface;
import com.gusparro.friggsys.domain.repositories.pagination.DomainPage;
import com.gusparro.friggsys.domain.repositories.pagination.PageParameters;
import com.gusparro.friggsys.usecase.user.dtos.UserOutput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindUsersUseCase Tests")
class FindUsersUseCaseTest {

    @Mock
    private UserRepositoryInterface repository;

    @InjectMocks
    private FindUsersUseCase useCase;

    @Mock
    private User user1;

    @Mock
    private User user2;

    @Mock
    private User user3;

    private PageParameters pageParameters;

    @BeforeEach
    void setUp() {
        pageParameters = PageParameters.builder()
                .page(0)
                .size(10)
                .build();
    }

    @Test
    @DisplayName("Should find users successfully with pagination")
    void shouldFindUsersSuccessfullyWithPagination() {
        var users = Arrays.asList(user1, user2, user3);
        var userPage = DomainPage.<User>builder()
                .data(users)
                .dataAmount(3L)
                .pagesAmount(1)
                .pageNumber(0)
                .pageSize(10)
                .firstPage(true)
                .lastPage(true)
                .build();

        when(repository.findAll(pageParameters)).thenReturn(userPage);

        var result = useCase.execute(pageParameters);

        assertNotNull(result);
        assertEquals(3, result.getData().size());
        assertEquals(3L, result.getDataAmount());
        assertEquals(1L, result.getPagesAmount());
        assertEquals(0, result.getPageNumber());
        assertEquals(10, result.getPageSize());
        assertTrue(result.isFirstPage());
        assertTrue(result.isLastPage());
        verify(repository, times(1)).findAll(pageParameters);
    }

    @Test
    @DisplayName("Should return empty page when no users exist")
    void shouldReturnEmptyPageWhenNoUsersExist() {
        var emptyPage = DomainPage.<User>builder()
                .data(Collections.emptyList())
                .dataAmount(0L)
                .pagesAmount(0)
                .pageNumber(0)
                .pageSize(10)
                .firstPage(true)
                .lastPage(true)
                .build();

        when(repository.findAll(pageParameters)).thenReturn(emptyPage);

        var result = useCase.execute(pageParameters);

        assertNotNull(result);
        assertTrue(result.getData().isEmpty());
        assertEquals(0L, result.getDataAmount());
        assertEquals(0L, result.getPagesAmount());
        verify(repository, times(1)).findAll(pageParameters);
    }

    @Test
    @DisplayName("Should handle first page correctly")
    void shouldHandleFirstPageCorrectly() {
        var users = Arrays.asList(user1, user2);
        var firstPage = DomainPage.<User>builder()
                .data(users)
                .dataAmount(20L)
                .pagesAmount(2)
                .pageNumber(0)
                .pageSize(10)
                .firstPage(true)
                .lastPage(false)
                .build();

        when(repository.findAll(pageParameters)).thenReturn(firstPage);

        var result = useCase.execute(pageParameters);

        assertNotNull(result);
        assertTrue(result.isFirstPage());
        assertFalse(result.isLastPage());
        assertEquals(0, result.getPageNumber());
        verify(repository, times(1)).findAll(pageParameters);
    }

    @Test
    @DisplayName("Should handle last page correctly")
    void shouldHandleLastPageCorrectly() {
       var lastPageParams = PageParameters.builder()
                .page(1)
                .size(10)
                .build();

        var users = Arrays.asList(user1, user2);
        var lastPage = DomainPage.<User>builder()
                .data(users)
                .dataAmount(20L)
                .pagesAmount(2)
                .pageNumber(1)
                .pageSize(10)
                .firstPage(false)
                .lastPage(true)
                .build();

        when(repository.findAll(lastPageParams)).thenReturn(lastPage);

        var result = useCase.execute(lastPageParams);

        assertNotNull(result);
        assertFalse(result.isFirstPage());
        assertTrue(result.isLastPage());
        assertEquals(1, result.getPageNumber());
        verify(repository, times(1)).findAll(lastPageParams);
    }

    @Test
    @DisplayName("Should handle middle page correctly")
    void shouldHandleMiddlePageCorrectly() {
       var middlePageParams = PageParameters.builder()
                .page(1)
                .size(10)
                .build();

        var users = Arrays.asList(user1, user2);
        var middlePage = DomainPage.<User>builder()
                .data(users)
                .dataAmount(30L)
                .pagesAmount(3)
                .pageNumber(1)
                .pageSize(10)
                .firstPage(false)
                .lastPage(false)
                .build();

        when(repository.findAll(middlePageParams)).thenReturn(middlePage);

        var result = useCase.execute(middlePageParams);

        assertNotNull(result);
        assertFalse(result.isFirstPage());
        assertFalse(result.isLastPage());
        assertEquals(1, result.getPageNumber());
        verify(repository, times(1)).findAll(middlePageParams);
    }

    @Test
    @DisplayName("Should convert all users to UserOutput")
    void shouldConvertAllUsersToUserOutput() {
        var users = Arrays.asList(user1, user2, user3);
        var userPage = DomainPage.<User>builder()
                .data(users)
                .dataAmount(3L)
                .pagesAmount(1)
                .pageNumber(0)
                .pageSize(10)
                .firstPage(true)
                .lastPage(true)
                .build();

        when(repository.findAll(pageParameters)).thenReturn(userPage);

        var result = useCase.execute(pageParameters);

        assertEquals(users.size(), result.getData().size());
        result.getData().forEach(Assertions::assertNotNull);
    }

    @Test
    @DisplayName("Should handle different page sizes")
    void shouldHandleDifferentPageSizes() {
        var customPageParams = PageParameters.builder()
                .page(0)
                .size(5)
                .build();

        var users = Arrays.asList(user1, user2);
        var customPage = DomainPage.<User>builder()
                .data(users)
                .dataAmount(10L)
                .pagesAmount(2)
                .pageNumber(0)
                .pageSize(5)
                .firstPage(true)
                .lastPage(false)
                .build();

        when(repository.findAll(customPageParams)).thenReturn(customPage);

        var result = useCase.execute(customPageParams);

        assertNotNull(result);
        assertEquals(5, result.getPageSize());
        assertEquals(2L, result.getPagesAmount());
        verify(repository, times(1)).findAll(customPageParams);
    }

    @Test
    @DisplayName("Should preserve page metadata from repository")
    void shouldPreservePageMetadataFromRepository() {
        var users = List.of(user1);
        var userPage = DomainPage.<User>builder()
                .data(users)
                .dataAmount(100L)
                .pagesAmount(10)
                .pageNumber(5)
                .pageSize(10)
                .firstPage(false)
                .lastPage(false)
                .build();

        var params = PageParameters.builder()
                .page(5)
                .size(10)
                .build();

        when(repository.findAll(params)).thenReturn(userPage);

        var result = useCase.execute(params);

        assertEquals(100L, result.getDataAmount());
        assertEquals(10L, result.getPagesAmount());
        assertEquals(5, result.getPageNumber());
        assertEquals(10, result.getPageSize());
        assertFalse(result.isFirstPage());
        assertFalse(result.isLastPage());
    }

    @Test
    @DisplayName("Should call repository findAll exactly once per execution")
    void shouldCallRepositoryFindAllExactlyOncePerExecution() {
       var userPage = DomainPage.<User>builder()
                .data(Collections.emptyList())
                .dataAmount(0L)
                .pagesAmount(0)
                .pageNumber(0)
                .pageSize(10)
                .firstPage(true)
                .lastPage(true)
                .build();

        when(repository.findAll(pageParameters)).thenReturn(userPage);

        useCase.execute(pageParameters);

        verify(repository, times(1)).findAll(pageParameters);
        verify(repository, times(1)).findAll(any(PageParameters.class));
    }

    @Test
    @DisplayName("Should handle null PageParameters gracefully")
    void shouldHandleNullPageParametersGracefully() {
        when(repository.findAll(null)).thenThrow(new IllegalArgumentException("PageParameters cannot be null"));

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
        verify(repository, times(1)).findAll(null);
    }

    @Test
    @DisplayName("Should maintain data count consistency")
    void shouldMaintainDataCountConsistency() {
        var users = Arrays.asList(user1, user2, user3);
        var userPage = DomainPage.<User>builder()
                .data(users)
                .dataAmount(3L)
                .pagesAmount(1)
                .pageNumber(0)
                .pageSize(10)
                .firstPage(true)
                .lastPage(true)
                .build();

        when(repository.findAll(pageParameters)).thenReturn(userPage);

        var result = useCase.execute(pageParameters);

        assertEquals(users.size(), result.getData().size());
        assertEquals(userPage.getDataAmount(), result.getDataAmount());
    }

    @Test
    @DisplayName("Should use provided PageParameters without modification")
    void shouldUseProvidedPageParametersWithoutModification() {
        var userPage = DomainPage.<User>builder()
                .data(Collections.emptyList())
                .dataAmount(0L)
                .pagesAmount(0)
                .pageNumber(0)
                .pageSize(10)
                .firstPage(true)
                .lastPage(true)
                .build();

        when(repository.findAll(pageParameters)).thenReturn(userPage);

        useCase.execute(pageParameters);

        verify(repository).findAll(eq(pageParameters));
    }

}