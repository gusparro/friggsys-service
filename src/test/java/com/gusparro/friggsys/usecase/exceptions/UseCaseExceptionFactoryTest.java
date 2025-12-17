package com.gusparro.friggsys.usecase.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UseCaseExceptionFactory Tests")
class UseCaseExceptionFactoryTest {

    @Test
    @DisplayName("Should create duplicate email error with correct message")
    void shouldCreateDuplicateEmailErrorWithCorrectMessage() {
        var email = "user@example.com";

        var error = UseCaseExceptionFactory.duplicateEmailError(email);

        assertEquals("A user with email 'user@example.com' address already exists.", error.getMessage());
        assertInstanceOf(DuplicateEmailError.class, error);
    }

    @Test
    @DisplayName("Should create duplicate email error with timestamp detail")
    void shouldCreateDuplicateEmailErrorWithTimestampDetail() {
        var error = UseCaseExceptionFactory.duplicateEmailError("test@example.com");

        assertNotNull(error.getDetails().get("timestamp"));
        assertInstanceOf(Instant.class, error.getDetails().get("timestamp"));
    }

    @Test
    @DisplayName("Should create duplicate email error with conflict type detail")
    void shouldCreateDuplicateEmailErrorWithConflictTypeDetail() {
        var error = UseCaseExceptionFactory.duplicateEmailError("test@example.com");

        assertEquals("Duplicate e-mail", error.getDetails().get("conflictType"));
    }

    @Test
    @DisplayName("Should create duplicate email error with all details")
    void shouldCreateDuplicateEmailErrorWithAllDetails() {
        var error = UseCaseExceptionFactory.duplicateEmailError("admin@company.com");

        assertEquals(2, error.getDetails().size());
        assertNotNull(error.getDetails().get("timestamp"));
        assertEquals("Duplicate e-mail", error.getDetails().get("conflictType"));
    }

    @Test
    @DisplayName("Should create entity not found error with correct message")
    void shouldCreateEntityNotFoundErrorWithCorrectMessage() {
        var error = UseCaseExceptionFactory.entityNotFoundError("User", "ID", "123", "find");

        assertEquals("User with 'ID' '123' not found", error.getMessage());
        assertInstanceOf(EntityNotFoundError.class, error);
    }

    @Test
    @DisplayName("Should create entity not found error with correct entity metadata")
    void shouldCreateEntityNotFoundErrorWithCorrectEntityMetadata() {
        var error = UseCaseExceptionFactory.entityNotFoundError("Order", "OrderNumber", "ORD-456", "delete");

        assertEquals("Order", error.getEntityName());
        assertEquals("OrderNumber", error.getIdentifierType());
        assertEquals("ORD-456", error.getIdentifier());
    }

    @Test
    @DisplayName("Should create entity not found error with all details")
    void shouldCreateEntityNotFoundErrorWithAllDetails() {
        var error = UseCaseExceptionFactory.entityNotFoundError("Product", "SKU", "PROD-789", "update");

        assertEquals(5, error.getDetails().size());
        assertNotNull(error.getDetails().get("searchedAt"));
        assertInstanceOf(Instant.class, error.getDetails().get("searchedAt"));
        assertEquals("Product", error.getDetails().get("resourceType"));
        assertEquals("SKU", error.getDetails().get("identifierType"));
        assertEquals("PROD-789", error.getDetails().get("identifier"));
        assertEquals("update", error.getDetails().get("operation"));
    }

    @Test
    @DisplayName("Should create entity not found error for different operations")
    void shouldCreateEntityNotFoundErrorForDifferentOperations() {
        var findError = UseCaseExceptionFactory.entityNotFoundError("User", "ID", "1", "find");
        var deleteError = UseCaseExceptionFactory.entityNotFoundError("User", "ID", "2", "delete");
        var updateError = UseCaseExceptionFactory.entityNotFoundError("User", "ID", "3", "update");

        assertEquals("find", findError.getDetails().get("operation"));
        assertEquals("delete", deleteError.getDetails().get("operation"));
        assertEquals("update", updateError.getDetails().get("operation"));
    }

    @Test
    @DisplayName("Should create matching error with correct message")
    void shouldCreateMatchingErrorWithCorrectMessage() {
        var error = UseCaseExceptionFactory.matchingError("User", "password", "login");

        assertEquals("'User' failed to match the password' field.", error.getMessage());
        assertInstanceOf(MatchingError.class, error);
    }

    @Test
    @DisplayName("Should create matching error with correct entity and field metadata")
    void shouldCreateMatchingErrorWithCorrectEntityAndFieldMetadata() {
        var error = UseCaseExceptionFactory.matchingError("Account", "email", "verify");

        assertEquals("Account", error.getEntityName());
        assertEquals("email", error.getFieldName());
    }

    @Test
    @DisplayName("Should create matching error with all details")
    void shouldCreateMatchingErrorWithAllDetails() {
        var error = UseCaseExceptionFactory.matchingError("User", "password", "authentication");

        assertEquals(4, error.getDetails().size());
        assertNotNull(error.getDetails().get("searchedAt"));
        assertInstanceOf(Instant.class, error.getDetails().get("searchedAt"));
        assertEquals("User", error.getDetails().get("resourceType"));
        assertEquals("password", error.getDetails().get("fieldName"));
        assertEquals("authentication", error.getDetails().get("operation"));
    }

    @Test
    @DisplayName("Should create matching error for different operations")
    void shouldCreateMatchingErrorForDifferentOperations() {
        var loginError = UseCaseExceptionFactory.matchingError("User", "password", "login");
        var verifyError = UseCaseExceptionFactory.matchingError("Account", "token", "verify");
        var resetError = UseCaseExceptionFactory.matchingError("User", "resetCode", "reset_password");

        assertEquals("login", loginError.getDetails().get("operation"));
        assertEquals("verify", verifyError.getDetails().get("operation"));
        assertEquals("reset_password", resetError.getDetails().get("operation"));
    }

    @Test
    @DisplayName("Should create unique timestamps for each error")
    void shouldCreateUniqueTimestampsForEachError() throws InterruptedException {
        var error1 = UseCaseExceptionFactory.duplicateEmailError("test1@example.com");
        Thread.sleep(10);
        var error2 = UseCaseExceptionFactory.duplicateEmailError("test2@example.com");

        Instant timestamp1 = (Instant) error1.getDetails().get("timestamp");
        Instant timestamp2 = (Instant) error2.getDetails().get("timestamp");

        assertNotEquals(timestamp1, timestamp2);
        assertTrue(timestamp2.isAfter(timestamp1));
    }

    @Test
    @DisplayName("Should create duplicate email error for different email addresses")
    void shouldCreateDuplicateEmailErrorForDifferentEmailAddresses() {
        var error1 = UseCaseExceptionFactory.duplicateEmailError("user1@example.com");
        var error2 = UseCaseExceptionFactory.duplicateEmailError("user2@example.com");
        var error3 = UseCaseExceptionFactory.duplicateEmailError("admin@company.org");

        assertTrue(error1.getMessage().contains("user1@example.com"));
        assertTrue(error2.getMessage().contains("user2@example.com"));
        assertTrue(error3.getMessage().contains("admin@company.org"));
    }

    @Test
    @DisplayName("Should create entity not found error for different entities")
    void shouldCreateEntityNotFoundErrorForDifferentEntities() {
        var userError = UseCaseExceptionFactory.entityNotFoundError("User", "ID", "1", "find");
        var orderError = UseCaseExceptionFactory.entityNotFoundError("Order", "OrderId", "ORD-1", "cancel");
        var productError = UseCaseExceptionFactory.entityNotFoundError("Product", "SKU", "SKU-1", "view");

        assertEquals("User", userError.getEntityName());
        assertEquals("Order", orderError.getEntityName());
        assertEquals("Product", productError.getEntityName());
    }

    @Test
    @DisplayName("Should create matching error for different fields")
    void shouldCreateMatchingErrorForDifferentFields() {
        var passwordError = UseCaseExceptionFactory.matchingError("User", "password", "login");
        var emailError = UseCaseExceptionFactory.matchingError("Account", "email", "verify");
        var tokenError = UseCaseExceptionFactory.matchingError("Session", "token", "validate");

        assertEquals("password", passwordError.getFieldName());
        assertEquals("email", emailError.getFieldName());
        assertEquals("token", tokenError.getFieldName());
    }

    @Test
    @DisplayName("Should create entity not found error with UUID identifier")
    void shouldCreateEntityNotFoundErrorWithUuidIdentifier() {
        var uuid = "550e8400-e29b-41d4-a716-446655440000";
        var error = UseCaseExceptionFactory.entityNotFoundError("User", "UUID", uuid, "activate");

        assertEquals(uuid, error.getIdentifier());
        assertEquals("UUID", error.getIdentifierType());
        assertEquals(uuid, error.getDetails().get("identifier"));
    }

    @Test
    @DisplayName("Should include operation in entity not found error details")
    void shouldIncludeOperationInEntityNotFoundErrorDetails() {
        var findError = UseCaseExceptionFactory.entityNotFoundError("User", "ID", "123", "find_by_id");
        var deleteError = UseCaseExceptionFactory.entityNotFoundError("Post", "Slug", "my-post", "delete");
        var updateError = UseCaseExceptionFactory.entityNotFoundError("Comment", "ID", "456", "update");

        assertEquals("find_by_id", findError.getDetails().get("operation"));
        assertEquals("delete", deleteError.getDetails().get("operation"));
        assertEquals("update", updateError.getDetails().get("operation"));
    }

    @Test
    @DisplayName("Should include operation in matching error details")
    void shouldIncludeOperationInMatchingErrorDetails() {
        var loginError = UseCaseExceptionFactory.matchingError("User", "password", "login");
        var changePasswordError = UseCaseExceptionFactory.matchingError("User", "currentPassword", "change_password");
        var verifyError = UseCaseExceptionFactory.matchingError("Account", "verificationCode", "verify_email");

        assertEquals("login", loginError.getDetails().get("operation"));
        assertEquals("change_password", changePasswordError.getDetails().get("operation"));
        assertEquals("verify_email", verifyError.getDetails().get("operation"));
    }

    @Test
    @DisplayName("Should create errors that are throwable")
    void shouldCreateErrorsThatAreThrowable() {
        assertThrows(DuplicateEmailError.class, () -> {
            throw UseCaseExceptionFactory.duplicateEmailError("test@example.com");
        });

        assertThrows(EntityNotFoundError.class, () -> {
            throw UseCaseExceptionFactory.entityNotFoundError("User", "ID", "123", "find");
        });

        assertThrows(MatchingError.class, () -> {
            throw UseCaseExceptionFactory.matchingError("User", "password", "login");
        });
    }

    @Test
    @DisplayName("Should create errors that extend UseCaseException")
    void shouldCreateErrorsThatExtendUseCaseException() {
        var duplicateError = UseCaseExceptionFactory.duplicateEmailError("test@example.com");
        var notFoundError = UseCaseExceptionFactory.entityNotFoundError("User", "ID", "123", "find");
        var matchingError = UseCaseExceptionFactory.matchingError("User", "password", "login");

        assertInstanceOf(UseCaseException.class, duplicateError);
        assertInstanceOf(UseCaseException.class, notFoundError);
        assertInstanceOf(UseCaseException.class, matchingError);
    }

    @Test
    @DisplayName("Should create errors that extend RuntimeException")
    void shouldCreateErrorsThatExtendRuntimeException() {
        var duplicateError = UseCaseExceptionFactory.duplicateEmailError("test@example.com");
        var notFoundError = UseCaseExceptionFactory.entityNotFoundError("User", "ID", "123", "find");
        var matchingError = UseCaseExceptionFactory.matchingError("User", "password", "login");

        assertInstanceOf(RuntimeException.class, duplicateError);
        assertInstanceOf(RuntimeException.class, notFoundError);
        assertInstanceOf(RuntimeException.class, matchingError);
    }

    @Test
    @DisplayName("Should create duplicate email error for email with special characters")
    void shouldCreateDuplicateEmailErrorForEmailWithSpecialCharacters() {
        var error = UseCaseExceptionFactory.duplicateEmailError("user+tag@example.com");

        assertTrue(error.getMessage().contains("user+tag@example.com"));
    }

    @Test
    @DisplayName("Should create matching error for typical password change scenario")
    void shouldCreateMatchingErrorForTypicalPasswordChangeScenario() {
        var error = UseCaseExceptionFactory.matchingError("User", "currentPassword", "change_password");

        assertEquals("'User' failed to match the currentPassword' field.", error.getMessage());
        assertEquals("User", error.getEntityName());
        assertEquals("currentPassword", error.getFieldName());
        assertEquals("change_password", error.getDetails().get("operation"));
        assertNotNull(error.getDetails().get("searchedAt"));
    }

    @Test
    @DisplayName("Should create entity not found error for typical use case scenario")
    void shouldCreateEntityNotFoundErrorForTypicalUseCaseScenario() {
        var error = UseCaseExceptionFactory.entityNotFoundError("User", "ID", "550e8400-e29b-41d4-a716-446655440000", "activate");

        assertEquals("User with 'ID' '550e8400-e29b-41d4-a716-446655440000' not found", error.getMessage());
        assertEquals("User", error.getEntityName());
        assertEquals("ID", error.getIdentifierType());
        assertEquals("550e8400-e29b-41d4-a716-446655440000", error.getIdentifier());
        assertEquals("activate", error.getDetails().get("operation"));
        assertEquals("User", error.getDetails().get("resourceType"));
    }

}