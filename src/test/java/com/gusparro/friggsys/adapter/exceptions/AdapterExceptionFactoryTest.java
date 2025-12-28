package com.gusparro.friggsys.adapter.exceptions;

import com.gusparro.friggsys.domain.exceptions.InvalidStateError;
import com.gusparro.friggsys.domain.exceptions.ValidationError;
import com.gusparro.friggsys.usecase.exceptions.EntityNotFoundError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AdapterExceptionFactory Tests")
class AdapterExceptionFactoryTest {

    @Test
    @DisplayName("Should create BadRequestError from InvalidStateError for status conflict")
    void shouldCreateBadRequestErrorFromInvalidStateErrorForStatusConflict() {
        Map<String, Object> details = Map.of("userId", "123", "currentStatus", "inactive");
        var invalidStateError = new InvalidStateError("User", "inactive", "delete", details);

        var badRequestError = AdapterExceptionFactory.statusConflict(invalidStateError);

        assertNotNull(badRequestError);
        assertEquals("It is not possible to execute 'delete' on User in the 'inactive' state",
                badRequestError.getMessage());
        assertEquals("User", badRequestError.getEntityName());
        assertEquals("delete", badRequestError.getAction());
        assertEquals(2, badRequestError.getDetails().size());
        assertEquals("123", badRequestError.getDetails().get("userId"));
        assertEquals("inactive", badRequestError.getDetails().get("currentStatus"));
    }

    @Test
    @DisplayName("Should create BadRequestError from ValidationError for invalid field")
    void shouldCreateBadRequestErrorFromValidationErrorForInvalidField() {
        Map<String, Object> details = Map.of("validationType", "pattern", "pattern", "^[a-z]+$");
        var validationError = new ValidationError("must contain only lowercase letters", "username", details);

        var badRequestError = AdapterExceptionFactory.invalidField("User", "create", validationError);

        assertNotNull(badRequestError);
        assertEquals("It is not possible to execute 'create' on User due a problem with the 'username' field, must contain only lowercase letters",
                badRequestError.getMessage());
        assertEquals("User", badRequestError.getEntityName());
        assertEquals("create", badRequestError.getAction());
        assertEquals(2, badRequestError.getDetails().size());
    }

    @Test
    @DisplayName("Should create BadRequestError from EntityNotFoundError")
    void shouldCreateBadRequestErrorFromEntityNotFoundError() {
        Map<String, Object> details = Map.of("searchedAt", Instant.now(), "operation", "update");
        var entityNotFoundError = new EntityNotFoundError("User", "ID", "123", details);

        var badRequestError = AdapterExceptionFactory.entityNotFound("update", entityNotFoundError);

        assertNotNull(badRequestError);
        assertEquals("User", badRequestError.getEntityName());
        assertEquals("update", badRequestError.getAction());
        assertEquals(2, badRequestError.getDetails().size());
    }

    @Test
    @DisplayName("Should create NotFoundError from EntityNotFoundError")
    void shouldCreateNotFoundErrorFromEntityNotFoundError() {
        Map<String, Object> details = Map.of("repository", "UserRepository", "attemptCount", 1);
        var entityNotFoundError = new EntityNotFoundError("User", "Email", "user@example.com", details);

        var notFoundError = AdapterExceptionFactory.resourceNotExists(entityNotFoundError);

        assertNotNull(notFoundError);
        assertEquals("User", notFoundError.getEntityName());
        assertEquals(2, notFoundError.getDetails().size());
        assertEquals("UserRepository", notFoundError.getDetails().get("repository"));
        assertEquals(1, notFoundError.getDetails().get("attemptCount"));
    }

    @Test
    @DisplayName("Should create ConflictError with all details")
    void shouldCreateConflictErrorWithAllDetails() {
        var conflictError = AdapterExceptionFactory.conflictError("User", "create", "duplicate_email");

        assertNotNull(conflictError);
        assertEquals("User", conflictError.getEntityName());
        assertEquals("create", conflictError.getAction());
        assertEquals("duplicate_email", conflictError.getConflictType());

        var details = conflictError.getDetails();
        assertEquals(4, details.size());
        assertEquals("duplicate_email", details.get("conflictType"));
        assertEquals("User", details.get("entity"));
        assertEquals("create", details.get("action"));
        assertNotNull(details.get("timestamp"));
        assertInstanceOf(Instant.class, details.get("timestamp"));
    }

    @Test
    @DisplayName("Should create ConflictError with timestamp")
    void shouldCreateConflictErrorWithTimestamp() {
        var before = Instant.now();
        var conflictError = AdapterExceptionFactory.conflictError("Order", "cancel", "already_shipped");
        var after = Instant.now();

        var timestamp = (Instant) conflictError.getDetails().get("timestamp");
        assertNotNull(timestamp);
        assertTrue(timestamp.isAfter(before.minusSeconds(1)));
        assertTrue(timestamp.isBefore(after.plusSeconds(1)));
    }

    @Test
    @DisplayName("Should create BadRequestError with null details from InvalidStateError")
    void shouldCreateBadRequestErrorWithNullDetailsFromInvalidStateError() {
        var invalidStateError = new InvalidStateError("Account", "suspended", "withdraw", null);

        var badRequestError = AdapterExceptionFactory.statusConflict(invalidStateError);

        assertNotNull(badRequestError);
        assertNotNull(badRequestError.getDetails());
    }

    @Test
    @DisplayName("Should create BadRequestError with null details from ValidationError")
    void shouldCreateBadRequestErrorWithNullDetailsFromValidationError() {
        var validationError = new ValidationError("is required", "email", null);

        var badRequestError = AdapterExceptionFactory.invalidField("User", "register", validationError);

        assertNotNull(badRequestError);
        assertNotNull(badRequestError.getDetails());
    }

    @Test
    @DisplayName("Should create BadRequestError with empty details from EntityNotFoundError")
    void shouldCreateBadRequestErrorWithEmptyDetailsFromEntityNotFoundError() {
        var entityNotFoundError = new EntityNotFoundError("Product", "SKU", "PROD-123", new HashMap<>());

        var badRequestError = AdapterExceptionFactory.entityNotFound("delete", entityNotFoundError);

        assertNotNull(badRequestError);
        assertNotNull(badRequestError.getDetails());
        assertTrue(badRequestError.getDetails().isEmpty());
    }

    @Test
    @DisplayName("Should format invalidField message correctly")
    void shouldFormatInvalidFieldMessageCorrectly() {
        var validationError = new ValidationError("must be at least 8 characters", "password", null);

        var badRequestError = AdapterExceptionFactory.invalidField("User", "change_password", validationError);

        assertEquals("It is not possible to execute 'change_password' on User due a problem with the 'password' field, must be at least 8 characters",
                badRequestError.getMessage());
    }

    @Test
    @DisplayName("Should preserve InvalidStateError details in BadRequestError")
    void shouldPreserveInvalidStateErrorDetailsInBadRequestError() {
        Map<String, Object> originalDetails = Map.of(
                "entityId", "UUID-123",
                "currentState", "pending",
                "attemptedState", "completed"
        );
        var invalidStateError = new InvalidStateError("Order", "pending", "ship", originalDetails);

        var badRequestError = AdapterExceptionFactory.statusConflict(invalidStateError);

        assertEquals(3, badRequestError.getDetails().size());
        assertEquals("UUID-123", badRequestError.getDetails().get("entityId"));
        assertEquals("pending", badRequestError.getDetails().get("currentState"));
        assertEquals("completed", badRequestError.getDetails().get("attemptedState"));
    }

    @Test
    @DisplayName("Should preserve ValidationError details in BadRequestError")
    void shouldPreserveValidationErrorDetailsInBadRequestError() {
        Map<String, Object> originalDetails = Map.of(
                "minLength", 8,
                "actualLength", 5,
                "validationType", "min_length"
        );
        var validationError = new ValidationError("too short", "password", originalDetails);

        var badRequestError = AdapterExceptionFactory.invalidField("User", "update", validationError);

        assertEquals(3, badRequestError.getDetails().size());
        assertEquals(8, badRequestError.getDetails().get("minLength"));
        assertEquals(5, badRequestError.getDetails().get("actualLength"));
        assertEquals("min_length", badRequestError.getDetails().get("validationType"));
    }

    @Test
    @DisplayName("Should preserve EntityNotFoundError details in NotFoundError")
    void shouldPreserveEntityNotFoundErrorDetailsInNotFoundError() {
        Map<String, Object> originalDetails = Map.of(
                "searchedAt", Instant.now(),
                "repository", "UserRepository",
                "operation", "findById"
        );
        var entityNotFoundError = new EntityNotFoundError("User", "ID", "999", originalDetails);

        var notFoundError = AdapterExceptionFactory.resourceNotExists(entityNotFoundError);

        assertEquals(3, notFoundError.getDetails().size());
        assertNotNull(notFoundError.getDetails().get("searchedAt"));
        assertEquals("UserRepository", notFoundError.getDetails().get("repository"));
        assertEquals("findById", notFoundError.getDetails().get("operation"));
    }

    @Test
    @DisplayName("Should create different ConflictErrors for different conflict types")
    void shouldCreateDifferentConflictErrorsForDifferentConflictTypes() {
        var error1 = AdapterExceptionFactory.conflictError("User", "create", "duplicate_email");
        var error2 = AdapterExceptionFactory.conflictError("User", "create", "duplicate_username");
        var error3 = AdapterExceptionFactory.conflictError("Order", "cancel", "already_completed");

        assertEquals("duplicate_email", error1.getConflictType());
        assertEquals("duplicate_username", error2.getConflictType());
        assertEquals("already_completed", error3.getConflictType());

        assertEquals("User", error1.getEntityName());
        assertEquals("User", error2.getEntityName());
        assertEquals("Order", error3.getEntityName());
    }

    @Test
    @DisplayName("Should create BadRequestError for different entity types")
    void shouldCreateBadRequestErrorForDifferentEntityTypes() {
        var userError = new InvalidStateError("User", "blocked", "login", null);
        var orderError = new InvalidStateError("Order", "cancelled", "ship", null);
        var accountError = new InvalidStateError("Account", "suspended", "withdraw", null);

        var userBadRequest = AdapterExceptionFactory.statusConflict(userError);
        var orderBadRequest = AdapterExceptionFactory.statusConflict(orderError);
        var accountBadRequest = AdapterExceptionFactory.statusConflict(accountError);

        assertEquals("User", userBadRequest.getEntityName());
        assertEquals("Order", orderBadRequest.getEntityName());
        assertEquals("Account", accountBadRequest.getEntityName());
    }

    @Test
    @DisplayName("Should create unique timestamps for each ConflictError")
    void shouldCreateUniqueTimestampsForEachConflictError() throws InterruptedException {
        var error1 = AdapterExceptionFactory.conflictError("User", "create", "duplicate");
        Thread.sleep(10);
        var error2 = AdapterExceptionFactory.conflictError("User", "create", "duplicate");

        var timestamp1 = (Instant) error1.getDetails().get("timestamp");
        var timestamp2 = (Instant) error2.getDetails().get("timestamp");

        assertNotEquals(timestamp1, timestamp2);
        assertTrue(timestamp2.isAfter(timestamp1));
    }

    @Test
    @DisplayName("Should handle ValidationError with null field")
    void shouldHandleValidationErrorWithNullField() {
        var validationError = new ValidationError("validation failed", null, null);

        var badRequestError = AdapterExceptionFactory.invalidField("Entity", "action", validationError);

        assertTrue(badRequestError.getMessage().contains("'null' field"));
    }

    @Test
    @DisplayName("Should create BadRequestError with correct action from entityNotFound")
    void shouldCreateBadRequestErrorWithCorrectActionFromEntityNotFound() {
        var entityNotFoundError = new EntityNotFoundError("User", "ID", "123", null);

        var badRequestForUpdate = AdapterExceptionFactory.entityNotFound("update", entityNotFoundError);
        var badRequestForDelete = AdapterExceptionFactory.entityNotFound("delete", entityNotFoundError);
        var badRequestForActivate = AdapterExceptionFactory.entityNotFound("activate", entityNotFoundError);

        assertEquals("update", badRequestForUpdate.getAction());
        assertEquals("delete", badRequestForDelete.getAction());
        assertEquals("activate", badRequestForActivate.getAction());
    }

    @Test
    @DisplayName("Should create ConflictError with correct entity and action combination")
    void shouldCreateConflictErrorWithCorrectEntityAndActionCombination() {
        var error = AdapterExceptionFactory.conflictError("Payment", "process", "insufficient_funds");

        assertEquals("Payment", error.getEntityName());
        assertEquals("process", error.getAction());
        assertEquals("insufficient_funds", error.getConflictType());
        assertEquals("Payment", error.getDetails().get("entity"));
        assertEquals("process", error.getDetails().get("action"));
        assertEquals("insufficient_funds", error.getDetails().get("conflictType"));
    }

}