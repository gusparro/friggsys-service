package com.gusparro.friggsys.adapter.api;

import com.gusparro.friggsys.adapter.api.facades.UserOperationsFacade;
import com.gusparro.friggsys.adapter.api.request.ChangePasswordRequest;
import com.gusparro.friggsys.adapter.api.request.CreateUserRequest;
import com.gusparro.friggsys.adapter.api.request.UpdateUserRequest;
import com.gusparro.friggsys.adapter.api.response.UserResponse;
import com.gusparro.friggsys.domain.repositories.pagination.PageOrder;
import com.gusparro.friggsys.domain.repositories.pagination.PageParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController Tests")
class UserControllerTest {

    @Mock
    private UserOperationsFacade userOperationsFacade;

    @InjectMocks
    private UserController controller;

    @Test
    @DisplayName("Should create user and return CREATED status")
    void shouldCreateUserAndReturnCreatedStatus() {
        var request = new CreateUserRequest("John Doe", "john@example.com", "+5563999999999", "password123");
        var userId = UUID.randomUUID();
        var now = OffsetDateTime.now();
        var response = new UserResponse(userId, "John Doe", "john@example.com", "+5563999999999", "Active", now, now);

        when(userOperationsFacade.create(request)).thenReturn(response);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setRequestURI("/api/users");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        var result = controller.create(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(userId, result.getBody().id());
        verify(userOperationsFacade, times(1)).create(request);
    }

    @Test
    @DisplayName("Should create user and return location header")
    void shouldCreateUserAndReturnLocationHeader() {
        var request = new CreateUserRequest("John Doe", "john@example.com", "+5563999999999", "password123");
        var userId = UUID.randomUUID();
        var now = OffsetDateTime.now();
        var response = new UserResponse(userId, "John Doe", "john@example.com", "+5563999999999", "Active", now, now);

        when(userOperationsFacade.create(request)).thenReturn(response);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setRequestURI("/api/users");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        var result = controller.create(request);

        assertNotNull(result.getHeaders().getLocation());
        assertTrue(result.getHeaders().getLocation().toString().contains(userId.toString()));
    }

    @Test
    @DisplayName("Should create user with correct request data")
    void shouldCreateUserWithCorrectRequestData() {
        var request = new CreateUserRequest("Jane Smith", "jane@example.com", "+5563988888888", "securePass456");
        var userId = UUID.randomUUID();
        var now = OffsetDateTime.now();
        var response = new UserResponse(userId, "Jane Smith", "jane@example.com", "+5563988888888", "Active", now, now);

        when(userOperationsFacade.create(request)).thenReturn(response);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setRequestURI("/api/users");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        var result = controller.create(request);

        assertNotNull(result.getBody());
        assertEquals("Jane Smith", result.getBody().name());
        assertEquals("jane@example.com", result.getBody().email());
        assertEquals("+5563988888888", result.getBody().telephone());
    }

    @Test
    @DisplayName("Should update user and return OK status")
    void shouldUpdateUserAndReturnOkStatus() {
        var userId = UUID.randomUUID();
        var request = new UpdateUserRequest("Updated Name", "updated@example.com", "+5563977777777");
        var now = OffsetDateTime.now();
        var response = new UserResponse(userId, "Updated Name", "updated@example.com", "+5563977777777", "Active", now, now);

        when(userOperationsFacade.update(userId, request)).thenReturn(response);

        var result = controller.update(userId, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Updated Name", result.getBody().name());
        verify(userOperationsFacade, times(1)).update(userId, request);
    }

    @Test
    @DisplayName("Should update user with correct ID and request")
    void shouldUpdateUserWithCorrectIdAndRequest() {
        var userId = UUID.randomUUID();
        var request = new UpdateUserRequest("New Name", "newemail@example.com", "+5563966666666");
        var now = OffsetDateTime.now();
        var response = new UserResponse(userId, "New Name", "newemail@example.com", "+5563966666666", "Active", now, now);

        when(userOperationsFacade.update(userId, request)).thenReturn(response);

        var result = controller.update(userId, request);

        assertNotNull(result.getBody());
        assertEquals(userId, result.getBody().id());
        assertEquals("New Name", result.getBody().name());
        assertEquals("newemail@example.com", result.getBody().email());
        assertEquals("+5563966666666", result.getBody().telephone());
    }

    @Test
    @DisplayName("Should change password and return OK status")
    void shouldChangePasswordAndReturnOkStatus() {
        var userId = UUID.randomUUID();
        var request = new ChangePasswordRequest("oldPassword", "newPassword123");
        var now = OffsetDateTime.now();
        var response = new UserResponse(userId, "John Doe", "john@example.com", "+5563999999999", "Active", now, now);

        when(userOperationsFacade.changePassword(userId, request)).thenReturn(response);

        var result = controller.changePassword(userId, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(userOperationsFacade, times(1)).changePassword(userId, request);
    }

    @Test
    @DisplayName("Should change password with correct user ID")
    void shouldChangePasswordWithCorrectUserId() {
        var userId = UUID.randomUUID();
        var request = new ChangePasswordRequest("currentPass", "newSecurePass");
        var now = OffsetDateTime.now();
        var response = new UserResponse(userId, "John Doe", "john@example.com", "+5563999999999", "Active", now, now);

        when(userOperationsFacade.changePassword(userId, request)).thenReturn(response);

        var result = controller.changePassword(userId, request);

        assertNotNull(result.getBody());
        assertEquals(userId, result.getBody().id());
    }

    @Test
    @DisplayName("Should activate user and return OK status")
    void shouldActivateUserAndReturnOkStatus() {
        var userId = UUID.randomUUID();
        var now = OffsetDateTime.now();
        var response = new UserResponse(userId, "John Doe", "john@example.com", "+5563999999999", "Active", now, now);

        when(userOperationsFacade.activate(userId)).thenReturn(response);

        var result = controller.activate(userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Active", result.getBody().status());
        verify(userOperationsFacade, times(1)).activate(userId);
    }

    @Test
    @DisplayName("Should activate user with correct ID")
    void shouldActivateUserWithCorrectId() {
        var userId = UUID.randomUUID();
        var now = OffsetDateTime.now();
        var response = new UserResponse(userId, "John Doe", "john@example.com", "+5563999999999", "Active", now, now);

        when(userOperationsFacade.activate(userId)).thenReturn(response);

        var result = controller.activate(userId);

        assertNotNull(result.getBody());
        assertEquals(userId, result.getBody().id());
        assertEquals("Active", result.getBody().status());
    }

    @Test
    @DisplayName("Should deactivate user and return OK status")
    void shouldDeactivateUserAndReturnOkStatus() {
        var userId = UUID.randomUUID();
        var now = OffsetDateTime.now();
        var response = new UserResponse(userId, "John Doe", "john@example.com", "+5563999999999", "Inactive", now, now);

        when(userOperationsFacade.deactivate(userId)).thenReturn(response);

        var result = controller.deactivate(userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Inactive", result.getBody().status());
        verify(userOperationsFacade, times(1)).deactivate(userId);
    }

    @Test
    @DisplayName("Should deactivate user with correct ID")
    void shouldDeactivateUserWithCorrectId() {
        var userId = UUID.randomUUID();
        var now = OffsetDateTime.now();
        var response = new UserResponse(userId, "John Doe", "john@example.com", "+5563999999999", "Inactive", now, now);

        when(userOperationsFacade.deactivate(userId)).thenReturn(response);

        var result = controller.deactivate(userId);

        assertNotNull(result.getBody());
        assertEquals(userId, result.getBody().id());
        assertEquals("Inactive", result.getBody().status());
    }

    @Test
    @DisplayName("Should block user and return OK status")
    void shouldBlockUserAndReturnOkStatus() {
        var userId = UUID.randomUUID();
        var now = OffsetDateTime.now();
        var response = new UserResponse(userId, "John Doe", "john@example.com", "+5563999999999", "Blocked", now, now);

        when(userOperationsFacade.block(userId)).thenReturn(response);

        var result = controller.block(userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Blocked", result.getBody().status());
        verify(userOperationsFacade, times(1)).block(userId);
    }

    @Test
    @DisplayName("Should block user with correct ID")
    void shouldBlockUserWithCorrectId() {
        var userId = UUID.randomUUID();
        var now = OffsetDateTime.now();
        var response = new UserResponse(userId, "John Doe", "john@example.com", "+5563999999999", "Blocked", now, now);

        when(userOperationsFacade.block(userId)).thenReturn(response);

        var result = controller.block(userId);

        assertNotNull(result.getBody());
        assertEquals(userId, result.getBody().id());
        assertEquals("Blocked", result.getBody().status());
    }

    @Test
    @DisplayName("Should delete user and return NO_CONTENT status")
    void shouldDeleteUserAndReturnNoContentStatus() {
        var userId = UUID.randomUUID();

        doNothing().when(userOperationsFacade).delete(userId);

        var result = controller.delete(userId);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        verify(userOperationsFacade, times(1)).delete(userId);
    }

    @Test
    @DisplayName("Should delete user with correct ID")
    void shouldDeleteUserWithCorrectId() {
        var userId = UUID.randomUUID();

        doNothing().when(userOperationsFacade).delete(userId);

        controller.delete(userId);

        verify(userOperationsFacade, times(1)).delete(userId);
    }

    @Test
    @DisplayName("Should find all users with default parameters")
    void shouldFindAllUsersWithDefaultParameters() {
        var now = OffsetDateTime.now();
        var user1 = new UserResponse(UUID.randomUUID(), "User 1", "user1@example.com", "+5563911111111", "Active", now, now);
        var user2 = new UserResponse(UUID.randomUUID(), "User 2", "user2@example.com", "+5563922222222", "Active", now, now);
        var users = List.of(user1, user2);

        when(userOperationsFacade.findAll(any(PageParameters.class))).thenReturn(users);

        var result = controller.findAll(0, 10, "name", PageOrder.ASC);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
        verify(userOperationsFacade, times(1)).findAll(any(PageParameters.class));
    }

    @Test
    @DisplayName("Should find all users with correct page parameters")
    void shouldFindAllUsersWithCorrectPageParameters() {
        var now = OffsetDateTime.now();
        var users = List.of(new UserResponse(UUID.randomUUID(), "User", "user@example.com", "+5563999999999", "Active", now, now));

        when(userOperationsFacade.findAll(any(PageParameters.class))).thenReturn(users);

        controller.findAll(2, 20, "email", PageOrder.DESC);

        var captor = ArgumentCaptor.forClass(PageParameters.class);
        verify(userOperationsFacade).findAll(captor.capture());

        var parameters = captor.getValue();
        assertEquals(2, parameters.getPage());
        assertEquals(20, parameters.getSize());
        assertEquals("email", parameters.getOrderBy());
        assertEquals(PageOrder.DESC, parameters.getDirection());
    }

    @Test
    @DisplayName("Should find all users with custom page size")
    void shouldFindAllUsersWithCustomPageSize() {
        var now = OffsetDateTime.now();
        var users = List.of(
                new UserResponse(UUID.randomUUID(), "User 1", "user1@example.com", "+5563911111111", "Active", now, now),
                new UserResponse(UUID.randomUUID(), "User 2", "user2@example.com", "+5563922222222", "Active", now, now),
                new UserResponse(UUID.randomUUID(), "User 3", "user3@example.com", "+5563933333333", "Active", now, now)
        );

        when(userOperationsFacade.findAll(any(PageParameters.class))).thenReturn(users);

        var result = controller.findAll(0, 50, "name", PageOrder.ASC);

        assertNotNull(result.getBody());
        assertEquals(3, result.getBody().size());
    }

    @Test
    @DisplayName("Should find all users with descending order")
    void shouldFindAllUsersWithDescendingOrder() {
        var now = OffsetDateTime.now();
        var users = List.of(new UserResponse(UUID.randomUUID(), "User", "user@example.com", "+5563999999999", "Active", now, now));

        when(userOperationsFacade.findAll(any(PageParameters.class))).thenReturn(users);

        controller.findAll(0, 10, "createdAt", PageOrder.DESC);

        var captor = ArgumentCaptor.forClass(PageParameters.class);
        verify(userOperationsFacade).findAll(captor.capture());

        assertEquals(PageOrder.DESC, captor.getValue().getDirection());
    }

    @Test
    @DisplayName("Should find user by ID and return OK status")
    void shouldFindUserByIdAndReturnOkStatus() {
        var userId = UUID.randomUUID();
        var now = OffsetDateTime.now();
        var response = new UserResponse(userId, "John Doe", "john@example.com", "+5563999999999", "Active", now, now);

        when(userOperationsFacade.findById(userId)).thenReturn(response);

        var result = controller.findById(userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(userId, result.getBody().id());
        verify(userOperationsFacade, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Should find user by ID with correct user data")
    void shouldFindUserByIdWithCorrectUserData() {
        var userId = UUID.randomUUID();
        var now = OffsetDateTime.now();
        var response = new UserResponse(userId, "Jane Smith", "jane@example.com", "+5563988888888", "Active", now, now);

        when(userOperationsFacade.findById(userId)).thenReturn(response);

        var result = controller.findById(userId);

        assertNotNull(result.getBody());
        assertEquals("Jane Smith", result.getBody().name());
        assertEquals("jane@example.com", result.getBody().email());
        assertEquals("+5563988888888", result.getBody().telephone());
        assertEquals("Active", result.getBody().status());
    }

    @Test
    @DisplayName("Should find user by email and return OK status")
    void shouldFindUserByEmailAndReturnOkStatus() {
        var userId = UUID.randomUUID();
        var email = "john@example.com";
        var now = OffsetDateTime.now();
        var response = new UserResponse(userId, "John Doe", email, "+5563999999999", "Active", now, now);

        when(userOperationsFacade.findByEmail(email)).thenReturn(response);

        var result = controller.findByEmail(email);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(email, result.getBody().email());
        verify(userOperationsFacade, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Should find user by email with correct user data")
    void shouldFindUserByEmailWithCorrectUserData() {
        var userId = UUID.randomUUID();
        var email = "admin@example.com";
        var now = OffsetDateTime.now();
        var response = new UserResponse(userId, "Admin User", email, "+5563977777777", "Active", now, now);

        when(userOperationsFacade.findByEmail(email)).thenReturn(response);

        var result = controller.findByEmail(email);

        assertNotNull(result.getBody());
        assertEquals(userId, result.getBody().id());
        assertEquals("Admin User", result.getBody().name());
        assertEquals(email, result.getBody().email());
        assertEquals("+5563977777777", result.getBody().telephone());
    }

    @Test
    @DisplayName("Should call facade for each operation exactly once")
    void shouldCallFacadeForEachOperationExactlyOnce() {
        var userId = UUID.randomUUID();
        var createRequest = new CreateUserRequest("User Name", "user@example.com", "+5563999999999", "password");
        var updateRequest = new UpdateUserRequest("Updated Name", "updated@example.com", "+5563988888888");
        var passwordRequest = new ChangePasswordRequest("old", "new");
        var now = OffsetDateTime.now();
        var userResponse = new UserResponse(userId, "User", "user@example.com", "+5563999999999", "Active", now, now);

        when(userOperationsFacade.create(any())).thenReturn(userResponse);
        when(userOperationsFacade.update(any(), any())).thenReturn(userResponse);
        when(userOperationsFacade.changePassword(any(), any())).thenReturn(userResponse);
        when(userOperationsFacade.activate(any())).thenReturn(userResponse);
        when(userOperationsFacade.deactivate(any())).thenReturn(userResponse);
        when(userOperationsFacade.block(any())).thenReturn(userResponse);
        when(userOperationsFacade.findById(any())).thenReturn(userResponse);
        when(userOperationsFacade.findByEmail(any())).thenReturn(userResponse);
        when(userOperationsFacade.findAll(any())).thenReturn(List.of(userResponse));

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setRequestURI("/api/users");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        controller.create(createRequest);
        controller.update(userId, updateRequest);
        controller.changePassword(userId, passwordRequest);
        controller.activate(userId);
        controller.deactivate(userId);
        controller.block(userId);
        controller.delete(userId);
        controller.findById(userId);
        controller.findByEmail("test@example.com");
        controller.findAll(0, 10, "name", PageOrder.ASC);

        verify(userOperationsFacade, times(1)).create(any());
        verify(userOperationsFacade, times(1)).update(any(), any());
        verify(userOperationsFacade, times(1)).changePassword(any(), any());
        verify(userOperationsFacade, times(1)).activate(any());
        verify(userOperationsFacade, times(1)).deactivate(any());
        verify(userOperationsFacade, times(1)).block(any());
        verify(userOperationsFacade, times(1)).delete(any());
        verify(userOperationsFacade, times(1)).findById(any());
        verify(userOperationsFacade, times(1)).findByEmail(any());
        verify(userOperationsFacade, times(1)).findAll(any());
    }

    @Test
    @DisplayName("Should handle empty list when finding all users")
    void shouldHandleEmptyListWhenFindingAllUsers() {
        when(userOperationsFacade.findAll(any(PageParameters.class))).thenReturn(List.of());

        var result = controller.findAll(0, 10, "name", PageOrder.ASC);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    @DisplayName("Should find all users with different sorting fields")
    void shouldFindAllUsersWithDifferentSortingFields() {
        var now = OffsetDateTime.now();
        var users = List.of(new UserResponse(UUID.randomUUID(), "User", "user@example.com", "+5563999999999", "Active", now, now));

        when(userOperationsFacade.findAll(any(PageParameters.class))).thenReturn(users);

        controller.findAll(0, 10, "email", PageOrder.ASC);
        controller.findAll(0, 10, "status", PageOrder.DESC);
        controller.findAll(0, 10, "createdAt", PageOrder.ASC);

        verify(userOperationsFacade, times(3)).findAll(any(PageParameters.class));
    }

    @Test
    @DisplayName("Should create user and facade should receive correct request")
    void shouldCreateUserAndFacadeShouldReceiveCorrectRequest() {
        var request = new CreateUserRequest("Test User", "test@example.com", "+5563999999999", "testpass123");
        var now = OffsetDateTime.now();
        var response = new UserResponse(UUID.randomUUID(), "Test User", "test@example.com", "+5563999999999", "Active", now, now);

        when(userOperationsFacade.create(request)).thenReturn(response);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setRequestURI("/api/users");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        controller.create(request);

        verify(userOperationsFacade).create(eq(request));
    }

    @Test
    @DisplayName("Should update user and facade should receive correct parameters")
    void shouldUpdateUserAndFacadeShouldReceiveCorrectParameters() {
        var userId = UUID.randomUUID();
        var request = new UpdateUserRequest("Updated Name", "updated@test.com", "+5563955555555");
        var now = OffsetDateTime.now();
        var response = new UserResponse(userId, "Updated Name", "updated@test.com", "+5563955555555", "Active", now, now);

        when(userOperationsFacade.update(userId, request)).thenReturn(response);

        controller.update(userId, request);

        verify(userOperationsFacade).update(eq(userId), eq(request));
    }

    @Test
    @DisplayName("Should handle multiple status transitions for same user")
    void shouldHandleMultipleStatusTransitionsForSameUser() {
        var userId = UUID.randomUUID();
        var now = OffsetDateTime.now();
        var activeResponse = new UserResponse(userId, "User", "user@example.com", "+5563999999999", "Active", now, now);
        var inactiveResponse = new UserResponse(userId, "User", "user@example.com", "+5563999999999", "Inactive", now, now);
        var blockedResponse = new UserResponse(userId, "User", "user@example.com", "+5563999999999", "Blocked", now, now);

        when(userOperationsFacade.activate(userId)).thenReturn(activeResponse);
        when(userOperationsFacade.deactivate(userId)).thenReturn(inactiveResponse);
        when(userOperationsFacade.block(userId)).thenReturn(blockedResponse);

        var activateResult = controller.activate(userId);
        var deactivateResult = controller.deactivate(userId);
        var blockResult = controller.block(userId);

        assertEquals("Active", activateResult.getBody().status());
        assertEquals("Inactive", deactivateResult.getBody().status());
        assertEquals("Blocked", blockResult.getBody().status());
    }

    @Test
    @DisplayName("Should return user with all fields populated")
    void shouldReturnUserWithAllFieldsPopulated() {
        var userId = UUID.randomUUID();
        var createdAt = OffsetDateTime.now().minusDays(5);
        var updatedAt = OffsetDateTime.now();
        var response = new UserResponse(
                userId,
                "Complete User",
                "complete@example.com",
                "+5563987654321",
                "Active",
                createdAt,
                updatedAt
        );

        when(userOperationsFacade.findById(userId)).thenReturn(response);

        var result = controller.findById(userId);

        assertNotNull(result.getBody());
        assertEquals(userId, result.getBody().id());
        assertEquals("Complete User", result.getBody().name());
        assertEquals("complete@example.com", result.getBody().email());
        assertEquals("+5563987654321", result.getBody().telephone());
        assertEquals("Active", result.getBody().status());
        assertEquals(createdAt, result.getBody().createdAt());
        assertEquals(updatedAt, result.getBody().updatedAt());
    }

    @Test
    @DisplayName("Should handle users with different telephone formats")
    void shouldHandleUsersWithDifferentTelephoneFormats() {
        var now = OffsetDateTime.now();
        var user1 = new UserResponse(UUID.randomUUID(), "User 1", "user1@example.com", "+5563999999999", "Active", now, now);
        var user2 = new UserResponse(UUID.randomUUID(), "User 2", "user2@example.com", "+55 63 99999-9999", "Active", now, now);
        var user3 = new UserResponse(UUID.randomUUID(), "User 3", "user3@example.com", "63999999999", "Active", now, now);

        when(userOperationsFacade.findById(user1.id())).thenReturn(user1);
        when(userOperationsFacade.findById(user2.id())).thenReturn(user2);
        when(userOperationsFacade.findById(user3.id())).thenReturn(user3);

        var result1 = controller.findById(user1.id());
        var result2 = controller.findById(user2.id());
        var result3 = controller.findById(user3.id());

        assertEquals("+5563999999999", result1.getBody().telephone());
        assertEquals("+55 63 99999-9999", result2.getBody().telephone());
        assertEquals("63999999999", result3.getBody().telephone());
    }

}