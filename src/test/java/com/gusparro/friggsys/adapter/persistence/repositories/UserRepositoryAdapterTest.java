package com.gusparro.friggsys.adapter.persistence.repositories;

import com.gusparro.friggsys.adapter.persistence.entities.UserEntity;
import com.gusparro.friggsys.adapter.persistence.mappers.UserPersistenceMapper;
import com.gusparro.friggsys.domain.entities.User;
import com.gusparro.friggsys.domain.repositories.pagination.PageOrder;
import com.gusparro.friggsys.domain.repositories.pagination.PageParameters;
import com.gusparro.friggsys.domain.vos.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserRepositoryAdapter Tests")
class UserRepositoryAdapterTest {

    @InjectMocks
    private UserRepositoryAdapter repositoryAdapter;

    @Mock
    private UserJpaRepository repository;

    @Mock
    private UserPersistenceMapper mapper;

    @Mock
    private User user;

    @Mock
    private UserEntity userEntity;

    @Test
    @DisplayName("Should save user successfully")
    void shouldSaveUserSuccessfully() {
        when(mapper.toEntity(user)).thenReturn(userEntity);
        when(repository.save(userEntity)).thenReturn(userEntity);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        var result = repositoryAdapter.save(user);

        assertNotNull(result);
        assertEquals(user, result);
        verify(mapper).toEntity(user);
        verify(repository).save(userEntity);
        verify(mapper).toDomain(userEntity);
    }

    @Test
    @DisplayName("Should find all users with pagination without sorting")
    void shouldFindAllUsersWithPaginationWithoutSorting() {
        var parameters = PageParameters.builder()
                .page(0)
                .size(10)
                .build();

        var entities = List.of(userEntity, userEntity);
        Page<UserEntity> page = new PageImpl<>(entities, PageRequest.of(0, 10), 2);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        var result = repositoryAdapter.findAll(parameters);

        assertNotNull(result);
        assertEquals(2, result.getData().size());
        assertEquals(2L, result.getDataAmount());
        assertEquals(1, result.getPagesAmount());
        assertEquals(0, result.getPageNumber());
        assertEquals(10, result.getPageSize());
        assertTrue(result.isFirstPage());
        assertTrue(result.isLastPage());
        verify(repository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should find all users with pagination and sorting")
    void shouldFindAllUsersWithPaginationAndSorting() {
        var parameters = PageParameters.builder()
                .page(0)
                .size(10)
                .orderBy("name")
                .direction(PageOrder.ASC)
                .build();

        var entities = List.of(userEntity);
        var pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        Page<UserEntity> page = new PageImpl<>(entities, pageable, 1);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        var result = repositoryAdapter.findAll(parameters);

        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals(1L, result.getDataAmount());
        verify(repository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should find all users with DESC sorting")
    void shouldFindAllUsersWithDescSorting() {
        var parameters = PageParameters.builder()
                .page(0)
                .size(10)
                .orderBy("createdAt")
                .direction(PageOrder.DESC)
                .build();

        var entities = List.of(userEntity, userEntity, userEntity);
        var pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<UserEntity> page = new PageImpl<>(entities, pageable, 3);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        var result = repositoryAdapter.findAll(parameters);

        assertNotNull(result);
        assertEquals(3, result.getData().size());
        verify(repository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should find all users returning empty page")
    void shouldFindAllUsersReturningEmptyPage() {
        var parameters = PageParameters.builder()
                .page(0)
                .size(10)
                .build();

        Page<UserEntity> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);

        when(repository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        var result = repositoryAdapter.findAll(parameters);

        assertNotNull(result);
        assertTrue(result.getData().isEmpty());
        assertEquals(0L, result.getDataAmount());
        assertEquals(0, result.getPagesAmount());
        verify(repository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should find user by id successfully")
    void shouldFindUserByIdSuccessfully() {
        var userId = UUID.randomUUID();

        when(repository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(mapper.toDomain(userEntity)).thenReturn(user);

        var result = repositoryAdapter.findById(userId);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(repository).findById(userId);
        verify(mapper).toDomain(userEntity);
    }

    @Test
    @DisplayName("Should return empty when user not found by id")
    void shouldReturnEmptyWhenUserNotFoundById() {
        var userId = UUID.randomUUID();

        when(repository.findById(userId)).thenReturn(Optional.empty());

        var result = repositoryAdapter.findById(userId);

        assertTrue(result.isEmpty());
        verify(repository).findById(userId);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("Should find user by email successfully")
    void shouldFindUserByEmailSuccessfully() {
        var email = Email.of("test@example.com");

        when(repository.findByEmail(email.getValue())).thenReturn(Optional.of(userEntity));
        when(mapper.toDomain(userEntity)).thenReturn(user);

        var result = repositoryAdapter.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(repository).findByEmail(email.getValue());
        verify(mapper).toDomain(userEntity);
    }

    @Test
    @DisplayName("Should return empty when user not found by email")
    void shouldReturnEmptyWhenUserNotFoundByEmail() {
        var email = Email.of("notfound@example.com");

        when(repository.findByEmail(email.getValue())).thenReturn(Optional.empty());

        var result = repositoryAdapter.findByEmail(email);

        assertTrue(result.isEmpty());
        verify(repository).findByEmail(email.getValue());
        verify(mapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("Should return true when user exists by id")
    void shouldReturnTrueWhenUserExistsById() {
        var userId = UUID.randomUUID();

        when(repository.existsById(userId)).thenReturn(true);

        var result = repositoryAdapter.existsById(userId);

        assertTrue(result);
        verify(repository).existsById(userId);
    }

    @Test
    @DisplayName("Should return false when user does not exist by id")
    void shouldReturnFalseWhenUserDoesNotExistById() {
        var userId = UUID.randomUUID();

        when(repository.existsById(userId)).thenReturn(false);

        var result = repositoryAdapter.existsById(userId);

        assertFalse(result);
        verify(repository).existsById(userId);
    }

    @Test
    @DisplayName("Should return true when user exists by email")
    void shouldReturnTrueWhenUserExistsByEmail() {
        var email = Email.of("existing@example.com");

        when(repository.existsByEmail(email.getValue())).thenReturn(true);

        var result = repositoryAdapter.existsByEmail(email);

        assertTrue(result);
        verify(repository).existsByEmail(email.getValue());
    }

    @Test
    @DisplayName("Should return false when user does not exist by email")
    void shouldReturnFalseWhenUserDoesNotExistByEmail() {
        var email = Email.of("nonexistent@example.com");

        when(repository.existsByEmail(email.getValue())).thenReturn(false);

        var result = repositoryAdapter.existsByEmail(email);

        assertFalse(result);
        verify(repository).existsByEmail(email.getValue());
    }

    @Test
    @DisplayName("Should delete user by id")
    void shouldDeleteUserById() {
        var userId = UUID.randomUUID();

        repositoryAdapter.delete(userId);

        verify(repository).deleteById(userId);
    }

    @Test
    @DisplayName("Should create pageable without sort when orderBy is null")
    void shouldCreatePageableWithoutSortWhenOrderByIsNull() {
        var parameters = PageParameters.builder()
                .page(1)
                .size(20)
                .orderBy(null)
                .build();

        var entities = List.of(userEntity);
        Page<UserEntity> page = new PageImpl<>(entities, PageRequest.of(1, 20), 1);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        repositoryAdapter.findAll(parameters);

        var pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findAll(pageableCaptor.capture());

        var capturedPageable = pageableCaptor.getValue();
        assertEquals(1, capturedPageable.getPageNumber());
        assertEquals(20, capturedPageable.getPageSize());
        assertTrue(capturedPageable.getSort().isUnsorted());
    }

    @Test
    @DisplayName("Should create pageable without sort when orderBy is blank")
    void shouldCreatePageableWithoutSortWhenOrderByIsBlank() {
        var parameters = PageParameters.builder()
                .page(0)
                .size(15)
                .orderBy("   ")
                .build();

        var entities = List.of(userEntity);
        Page<UserEntity> page = new PageImpl<>(entities, PageRequest.of(0, 15), 1);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        repositoryAdapter.findAll(parameters);

        var pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findAll(pageableCaptor.capture());

        var capturedPageable = pageableCaptor.getValue();
        assertTrue(capturedPageable.getSort().isUnsorted());
    }

    @Test
    @DisplayName("Should create pageable with sort when orderBy is provided")
    void shouldCreatePageableWithSortWhenOrderByIsProvided() {
        var parameters = PageParameters.builder()
                .page(0)
                .size(10)
                .orderBy("email")
                .direction(PageOrder.ASC)
                .build();

        var entities = List.of(userEntity);
        Page<UserEntity> page = new PageImpl<>(entities, PageRequest.of(0, 10), 1);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        repositoryAdapter.findAll(parameters);

        var pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findAll(pageableCaptor.capture());

        var capturedPageable = pageableCaptor.getValue();
        assertTrue(capturedPageable.getSort().isSorted());
        assertEquals(Sort.Direction.ASC, capturedPageable.getSort().getOrderFor("email").getDirection());
    }

    @Test
    @DisplayName("Should handle pagination with first page correctly")
    void shouldHandlePaginationWithFirstPageCorrectly() {
        var parameters = PageParameters.builder()
                .page(0)
                .size(5)
                .build();

        var entities = List.of(userEntity, userEntity, userEntity, userEntity, userEntity);
        Page<UserEntity> page = new PageImpl<>(entities, PageRequest.of(0, 5), 15);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        var result = repositoryAdapter.findAll(parameters);

        assertTrue(result.isFirstPage());
        assertFalse(result.isLastPage());
        assertEquals(3, result.getPagesAmount());
    }

    @Test
    @DisplayName("Should handle pagination with last page correctly")
    void shouldHandlePaginationWithLastPageCorrectly() {
        var parameters = PageParameters.builder()
                .page(2)
                .size(5)
                .build();

        var entities = List.of(userEntity, userEntity);
        Page<UserEntity> page = new PageImpl<>(entities, PageRequest.of(2, 5), 12);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        var result = repositoryAdapter.findAll(parameters);

        assertFalse(result.isFirstPage());
        assertTrue(result.isLastPage());
        assertEquals(2, result.getPageNumber());
    }

    @Test
    @DisplayName("Should handle pagination with middle page correctly")
    void shouldHandlePaginationWithMiddlePageCorrectly() {
        var parameters = PageParameters.builder()
                .page(1)
                .size(10)
                .build();

        var entities = List.of(userEntity, userEntity, userEntity);
        Page<UserEntity> page = new PageImpl<>(entities, PageRequest.of(1, 10), 25);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        var result = repositoryAdapter.findAll(parameters);

        assertFalse(result.isFirstPage());
        assertFalse(result.isLastPage());
        assertEquals(1, result.getPageNumber());
    }

    @Test
    @DisplayName("Should map all entities to domain correctly")
    void shouldMapAllEntitiesToDomainCorrectly() {
        var parameters = PageParameters.builder()
                .page(0)
                .size(3)
                .build();

        var entity1 = mock(UserEntity.class);
        var entity2 = mock(UserEntity.class);
        var entity3 = mock(UserEntity.class);
        var user1 = mock(User.class);
        var user2 = mock(User.class);
        var user3 = mock(User.class);

        var entities = List.of(entity1, entity2, entity3);
        Page<UserEntity> page = new PageImpl<>(entities, PageRequest.of(0, 3), 3);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.toDomain(entity1)).thenReturn(user1);
        when(mapper.toDomain(entity2)).thenReturn(user2);
        when(mapper.toDomain(entity3)).thenReturn(user3);

        var result = repositoryAdapter.findAll(parameters);

        assertEquals(3, result.getData().size());
        verify(mapper).toDomain(entity1);
        verify(mapper).toDomain(entity2);
        verify(mapper).toDomain(entity3);
    }

    @Test
    @DisplayName("Should handle different page sizes correctly")
    void shouldHandleDifferentPageSizesCorrectly() {
        var parameters5 = PageParameters.builder().page(0).size(5).build();
        var parameters10 = PageParameters.builder().page(0).size(10).build();
        var parameters20 = PageParameters.builder().page(0).size(20).build();

        var entities5 = List.of(userEntity, userEntity, userEntity, userEntity, userEntity);
        var entities10 = List.of(userEntity, userEntity, userEntity);
        var entities20 = List.of(userEntity);

        Page<UserEntity> page5 = new PageImpl<>(entities5, PageRequest.of(0, 5), 5);
        Page<UserEntity> page10 = new PageImpl<>(entities10, PageRequest.of(0, 10), 3);
        Page<UserEntity> page20 = new PageImpl<>(entities20, PageRequest.of(0, 20), 1);

        when(repository.findAll(any(Pageable.class)))
                .thenReturn(page5)
                .thenReturn(page10)
                .thenReturn(page20);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        var result5 = repositoryAdapter.findAll(parameters5);
        var result10 = repositoryAdapter.findAll(parameters10);
        var result20 = repositoryAdapter.findAll(parameters20);

        assertEquals(5, result5.getPageSize());
        assertEquals(10, result10.getPageSize());
        assertEquals(20, result20.getPageSize());
    }

    @Test
    @DisplayName("Should handle multiple sort fields")
    void shouldHandleMultipleSortFields() {
        var parameters = PageParameters.builder()
                .page(0)
                .size(10)
                .orderBy("name")
                .direction(PageOrder.ASC)
                .build();

        var entities = List.of(userEntity);
        Page<UserEntity> page = new PageImpl<>(entities, PageRequest.of(0, 10), 1);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        repositoryAdapter.findAll(parameters);

        var pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findAll(pageableCaptor.capture());

        var capturedPageable = pageableCaptor.getValue();
        assertNotNull(capturedPageable.getSort().getOrderFor("name"));
    }

    @Test
    @DisplayName("Should save and return mapped user")
    void shouldSaveAndReturnMappedUser() {
        var savedEntity = mock(UserEntity.class);
        var savedUser = mock(User.class);

        when(mapper.toEntity(user)).thenReturn(userEntity);
        when(repository.save(userEntity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(savedUser);

        var result = repositoryAdapter.save(user);

        assertEquals(savedUser, result);
        verify(mapper).toEntity(user);
        verify(repository).save(userEntity);
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    @DisplayName("Should call repository delete with correct id")
    void shouldCallRepositoryDeleteWithCorrectId() {
        var userId1 = UUID.randomUUID();
        var userId2 = UUID.randomUUID();

        repositoryAdapter.delete(userId1);
        repositoryAdapter.delete(userId2);

        verify(repository).deleteById(userId1);
        verify(repository).deleteById(userId2);
    }

    @Test
    @DisplayName("Should find by different email addresses")
    void shouldFindByDifferentEmailAddresses() {
        var email1 = Email.of("user1@example.com");
        var email2 = Email.of("user2@example.com");

        when(repository.findByEmail(email1.getValue())).thenReturn(Optional.of(userEntity));
        when(repository.findByEmail(email2.getValue())).thenReturn(Optional.empty());
        when(mapper.toDomain(userEntity)).thenReturn(user);

        var result1 = repositoryAdapter.findByEmail(email1);
        var result2 = repositoryAdapter.findByEmail(email2);

        assertTrue(result1.isPresent());
        assertTrue(result2.isEmpty());
        verify(repository).findByEmail(email1.getValue());
        verify(repository).findByEmail(email2.getValue());
    }

    @Test
    @DisplayName("Should check existence by different emails")
    void shouldCheckExistenceByDifferentEmails() {
        var existingEmail = Email.of("exists@example.com");
        var nonExistingEmail = Email.of("notexists@example.com");

        when(repository.existsByEmail(existingEmail.getValue())).thenReturn(true);
        when(repository.existsByEmail(nonExistingEmail.getValue())).thenReturn(false);

        var exists = repositoryAdapter.existsByEmail(existingEmail);
        var notExists = repositoryAdapter.existsByEmail(nonExistingEmail);

        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    @DisplayName("Should handle pagination with single item")
    void shouldHandlePaginationWithSingleItem() {
        var parameters = PageParameters.builder()
                .page(0)
                .size(10)
                .build();

        var entities = List.of(userEntity);
        Page<UserEntity> page = new PageImpl<>(entities, PageRequest.of(0, 10), 1);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        var result = repositoryAdapter.findAll(parameters);

        assertEquals(1, result.getData().size());
        assertEquals(1L, result.getDataAmount());
        assertEquals(1, result.getPagesAmount());
        assertTrue(result.isFirstPage());
        assertTrue(result.isLastPage());
    }

    @Test
    @DisplayName("Should handle different ordering directions")
    void shouldHandleDifferentOrderingDirections() {
        var ascParameters = PageParameters.builder()
                .page(0)
                .size(10)
                .orderBy("name")
                .direction(PageOrder.ASC)
                .build();

        var descParameters = PageParameters.builder()
                .page(0)
                .size(10)
                .orderBy("name")
                .direction(PageOrder.DESC)
                .build();

        var entities = List.of(userEntity);
        Page<UserEntity> page = new PageImpl<>(entities, PageRequest.of(0, 10), 1);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        repositoryAdapter.findAll(ascParameters);
        repositoryAdapter.findAll(descParameters);

        verify(repository, times(2)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should return correct total elements count")
    void shouldReturnCorrectTotalElementsCount() {
        var parameters = PageParameters.builder()
                .page(0)
                .size(5)
                .build();

        var entities = List.of(userEntity, userEntity, userEntity);
        Page<UserEntity> page = new PageImpl<>(entities, PageRequest.of(0, 5), 23);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        var result = repositoryAdapter.findAll(parameters);

        assertEquals(23L, result.getDataAmount());
        assertEquals(3, result.getData().size());
    }

    @Test
    @DisplayName("Should find by id with different UUIDs")
    void shouldFindByIdWithDifferentUUIDs() {
        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();
        var entity1 = mock(UserEntity.class);
        var user1 = mock(User.class);

        when(repository.findById(id1)).thenReturn(Optional.of(entity1));
        when(repository.findById(id2)).thenReturn(Optional.empty());
        when(mapper.toDomain(entity1)).thenReturn(user1);

        var result1 = repositoryAdapter.findById(id1);
        var result2 = repositoryAdapter.findById(id2);

        assertTrue(result1.isPresent());
        assertEquals(user1, result1.get());
        assertTrue(result2.isEmpty());
    }

}