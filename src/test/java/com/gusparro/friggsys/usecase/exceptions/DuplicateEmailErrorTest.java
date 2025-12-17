package com.gusparro.friggsys.usecase.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DuplicateEmailError Tests")
class DuplicateEmailErrorTest {

    @Test
    @DisplayName("Should create exception with message and details")
    void shouldCreateExceptionWithMessageAndDetails() {
        Map<String, Object> details = new HashMap<>();
        details.put("email", "user@example.com");
        details.put("existingUserId", "123");

        var error = new DuplicateEmailError("Email already exists", details);

        assertEquals("Email already exists", error.getMessage());
        assertEquals(2, error.getDetails().size());
        assertEquals("user@example.com", error.getDetails().get("email"));
        assertEquals("123", error.getDetails().get("existingUserId"));
    }

    @Test
    @DisplayName("Should create exception with message and empty details")
    void shouldCreateExceptionWithMessageAndEmptyDetails() {
        Map<String, Object> details = new HashMap<>();

        var error = new DuplicateEmailError("Duplicate email detected", details);

        assertEquals("Duplicate email detected", error.getMessage());
        assertNotNull(error.getDetails());
        assertTrue(error.getDetails().isEmpty());
    }

    @Test
    @DisplayName("Should create exception with message and null details")
    void shouldCreateExceptionWithMessageAndNullDetails() {
        var error = new DuplicateEmailError("Email conflict", null);

        assertEquals("Email conflict", error.getMessage());
        assertNotNull(error.getDetails());
        assertTrue(error.getDetails().isEmpty());
    }

    @Test
    @DisplayName("Should extend UseCaseException")
    void shouldExtendUseCaseException() {
        var error = new DuplicateEmailError("Test", null);

        assertInstanceOf(UseCaseException.class, error);
        assertInstanceOf(RuntimeException.class, error);
    }

    @Test
    @DisplayName("Should be throwable")
    void shouldBeThrowable() {
        assertThrows(DuplicateEmailError.class, () -> {
            throw new DuplicateEmailError("Email already in use", null);
        });
    }

    @Test
    @DisplayName("Should allow adding details after creation")
    void shouldAllowAddingDetailsAfterCreation() {
        var error = new DuplicateEmailError("Duplicate email", new HashMap<>());

        error.addDetail("attemptedEmail", "new@example.com");
        error.addDetail("timestamp", "2024-01-01T10:00:00");

        assertEquals(2, error.getDetails().size());
        assertEquals("new@example.com", error.getDetails().get("attemptedEmail"));
        assertEquals("2024-01-01T10:00:00", error.getDetails().get("timestamp"));
    }

    @Test
    @DisplayName("Should handle multiple detail types")
    void shouldHandleMultipleDetailTypes() {
        Map<String, Object> details = new HashMap<>();
        details.put("email", "user@domain.com");
        details.put("userId", 456);
        details.put("active", true);
        details.put("attempts", 3);

        var error = new DuplicateEmailError("Email conflict", details);

        assertEquals(4, error.getDetails().size());
        assertEquals("user@domain.com", error.getDetails().get("email"));
        assertEquals(456, error.getDetails().get("userId"));
        assertEquals(true, error.getDetails().get("active"));
        assertEquals(3, error.getDetails().get("attempts"));
    }

    @Test
    @DisplayName("Should have no cause by default")
    void shouldHaveNoCauseByDefault() {
        var error = new DuplicateEmailError("Duplicate", null);

        assertNull(error.getCause());
    }

    @Test
    @DisplayName("Should create exception with typical use case scenario")
    void shouldCreateExceptionWithTypicalUseCaseScenario() {
        var duplicateEmail = "john.doe@example.com";
        Map<String, Object> details = Map.of(
                "email", duplicateEmail,
                "operation", "user_registration",
                "existingUserId", "UUID-123-456"
        );

        var error = new DuplicateEmailError(
                String.format("Email '%s' is already registered", duplicateEmail),
                details
        );

        assertEquals("Email 'john.doe@example.com' is already registered", error.getMessage());
        assertEquals(3, error.getDetails().size());
        assertEquals(duplicateEmail, error.getDetails().get("email"));
        assertEquals("user_registration", error.getDetails().get("operation"));
        assertEquals("UUID-123-456", error.getDetails().get("existingUserId"));
    }

    @Test
    @DisplayName("Should allow catching as UseCaseException")
    void shouldAllowCatchingAsUseCaseException() {
        try {
            throw new DuplicateEmailError("Email exists", null);
        } catch (UseCaseException e) {
            assertInstanceOf(DuplicateEmailError.class, e);
            assertEquals("Email exists", e.getMessage());
        }
    }

}