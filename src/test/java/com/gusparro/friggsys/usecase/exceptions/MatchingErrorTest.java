package com.gusparro.friggsys.usecase.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MatchingError Tests")
class MatchingErrorTest {

    @Test
    @DisplayName("Should create exception with entity name, field name and details")
    void shouldCreateExceptionWithEntityNameFieldNameAndDetails() {
        Map<String, Object> details = Map.of("expectedValue", "hashed_password", "providedValue", "wrong_password");

        var error = new MatchingError("User", "password", details);

        assertEquals("'User' failed to match the password' field.", error.getMessage());
        assertEquals("User", error.getEntityName());
        assertEquals("password", error.getFieldName());
        assertEquals(2, error.getDetails().size());
        assertEquals("hashed_password", error.getDetails().get("expectedValue"));
        assertEquals("wrong_password", error.getDetails().get("providedValue"));
    }

    @Test
    @DisplayName("Should create exception with null details")
    void shouldCreateExceptionWithNullDetails() {
        var error = new MatchingError("Account", "email", null);

        assertEquals("'Account' failed to match the email' field.", error.getMessage());
        assertEquals("Account", error.getEntityName());
        assertEquals("email", error.getFieldName());
        assertNotNull(error.getDetails());
        assertTrue(error.getDetails().isEmpty());
    }

    @Test
    @DisplayName("Should create exception with empty details")
    void shouldCreateExceptionWithEmptyDetails() {
        var error = new MatchingError("Document", "checksum", new HashMap<>());

        assertEquals("'Document' failed to match the checksum' field.", error.getMessage());
        assertEquals("Document", error.getEntityName());
        assertEquals("checksum", error.getFieldName());
        assertTrue(error.getDetails().isEmpty());
    }

    @Test
    @DisplayName("Should format message correctly for password matching")
    void shouldFormatMessageCorrectlyForPasswordMatching() {
        var error = new MatchingError("User", "password", null);

        assertEquals("'User' failed to match the password' field.", error.getMessage());
    }

    @Test
    @DisplayName("Should format message correctly for token matching")
    void shouldFormatMessageCorrectlyForTokenMatching() {
        var error = new MatchingError("Session", "token", null);

        assertEquals("'Session' failed to match the token' field.", error.getMessage());
    }

    @Test
    @DisplayName("Should format message correctly for verification code matching")
    void shouldFormatMessageCorrectlyForVerificationCodeMatching() {
        var error = new MatchingError("Account", "verificationCode", null);

        assertEquals("'Account' failed to match the verificationCode' field.", error.getMessage());
    }

    @Test
    @DisplayName("Should extend UseCaseException")
    void shouldExtendUseCaseException() {
        var error = new MatchingError("Entity", "field", null);

        assertInstanceOf(UseCaseException.class, error);
        assertInstanceOf(RuntimeException.class, error);
    }

    @Test
    @DisplayName("Should be throwable")
    void shouldBeThrowable() {
        assertThrows(MatchingError.class, () -> {
            throw new MatchingError("User", "password", null);
        });
    }

    @Test
    @DisplayName("Should allow adding details after creation")
    void shouldAllowAddingDetailsAfterCreation() {
        var error = new MatchingError("User", "email", new HashMap<>());

        error.addDetail("attemptCount", 3);
        error.addDetail("timestamp", "2024-01-15T10:30:00");
        error.addDetail("ipAddress", "192.168.1.1");

        assertEquals(3, error.getDetails().size());
        assertEquals(3, error.getDetails().get("attemptCount"));
        assertEquals("2024-01-15T10:30:00", error.getDetails().get("timestamp"));
        assertEquals("192.168.1.1", error.getDetails().get("ipAddress"));
    }

    @Test
    @DisplayName("Should have no cause by default")
    void shouldHaveNoCauseByDefault() {
        var error = new MatchingError("User", "password", null);

        assertNull(error.getCause());
    }

    @Test
    @DisplayName("Should store entity and field metadata correctly")
    void shouldStoreEntityAndFieldMetadataCorrectly() {
        var error = new MatchingError("Payment", "transactionHash", null);

        assertEquals("Payment", error.getEntityName());
        assertEquals("transactionHash", error.getFieldName());
    }

    @Test
    @DisplayName("Should handle different entity names")
    void shouldHandleDifferentEntityNames() {
        var error1 = new MatchingError("User", "password", null);
        var error2 = new MatchingError("Account", "pin", null);
        var error3 = new MatchingError("Session", "token", null);

        assertEquals("User", error1.getEntityName());
        assertEquals("Account", error2.getEntityName());
        assertEquals("Session", error3.getEntityName());
    }

    @Test
    @DisplayName("Should handle different field names")
    void shouldHandleDifferentFieldNames() {
        var error1 = new MatchingError("User", "password", null);
        var error2 = new MatchingError("User", "email", null);
        var error3 = new MatchingError("User", "phoneNumber", null);

        assertEquals("password", error1.getFieldName());
        assertEquals("email", error2.getFieldName());
        assertEquals("phoneNumber", error3.getFieldName());
    }

    @Test
    @DisplayName("Should create exception with complex details")
    void shouldCreateExceptionWithComplexDetails() {
        Map<String, Object> details = new HashMap<>();
        details.put("attemptedValue", "user_input");
        details.put("expectedPattern", "^[A-Z0-9]+$");
        details.put("attemptNumber", 5);
        details.put("accountLocked", false);
        details.put("lastAttempt", Map.of("timestamp", "2024-01-15", "result", "failed"));

        var error = new MatchingError("Credential", "apiKey", details);

        assertEquals(5, error.getDetails().size());
        assertEquals("user_input", error.getDetails().get("attemptedValue"));
        assertEquals("^[A-Z0-9]+$", error.getDetails().get("expectedPattern"));
        assertEquals(5, error.getDetails().get("attemptNumber"));
        assertEquals(false, error.getDetails().get("accountLocked"));
        assertTrue(error.getDetails().get("lastAttempt") instanceof Map);
    }

    @Test
    @DisplayName("Should allow catching as UseCaseException")
    void shouldAllowCatchingAsUseCaseException() {
        try {
            throw new MatchingError("User", "password", null);
        } catch (UseCaseException e) {
            assertInstanceOf(MatchingError.class, e);
            assertEquals("'User' failed to match the password' field.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Should allow catching as RuntimeException")
    void shouldAllowCatchingAsRuntimeException() {
        try {
            throw new MatchingError("Account", "securityQuestion", null);
        } catch (RuntimeException e) {
            assertInstanceOf(MatchingError.class, e);
            assertEquals("'Account' failed to match the securityQuestion' field.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Should create exception for typical password matching scenario")
    void shouldCreateExceptionForTypicalPasswordMatchingScenario() {
        Map<String, Object> details = Map.of(
                "operation", "authentication",
                "userId", "12345",
                "attemptCount", 2,
                "maxAttempts", 5
        );

        var error = new MatchingError("User", "password", details);

        assertEquals("'User' failed to match the password' field.", error.getMessage());
        assertEquals("User", error.getEntityName());
        assertEquals("password", error.getFieldName());
        assertEquals(4, error.getDetails().size());
        assertEquals("authentication", error.getDetails().get("operation"));
        assertEquals("12345", error.getDetails().get("userId"));
        assertEquals(2, error.getDetails().get("attemptCount"));
        assertEquals(5, error.getDetails().get("maxAttempts"));
    }

    @Test
    @DisplayName("Should create exception for token matching scenario")
    void shouldCreateExceptionForTokenMatchingScenario() {
        Map<String, Object> details = Map.of(
                "tokenType", "JWT",
                "expectedIssuer", "auth-service",
                "actualIssuer", "unknown"
        );

        var error = new MatchingError("Token", "signature", details);

        assertEquals("'Token' failed to match the signature' field.", error.getMessage());
        assertEquals("Token", error.getEntityName());
        assertEquals("signature", error.getFieldName());
        assertEquals("JWT", error.getDetails().get("tokenType"));
    }

    @Test
    @DisplayName("Should create exception for verification code matching scenario")
    void shouldCreateExceptionForVerificationCodeMatchingScenario() {
        Map<String, Object> details = Map.of(
                "codeType", "email_verification",
                "expiresAt", "2024-01-15T12:00:00",
                "isExpired", false
        );

        var error = new MatchingError("Account", "verificationCode", details);

        assertEquals("'Account' failed to match the verificationCode' field.", error.getMessage());
        assertEquals("Account", error.getEntityName());
        assertEquals("verificationCode", error.getFieldName());
        assertEquals("email_verification", error.getDetails().get("codeType"));
    }

    @Test
    @DisplayName("Should maintain immutability of entity and field metadata")
    void shouldMaintainImmutabilityOfEntityAndFieldMetadata() {
        var error = new MatchingError("User", "password", null);

        assertEquals("User", error.getEntityName());
        assertEquals("password", error.getFieldName());

        assertEquals("User", error.getEntityName());
        assertEquals("password", error.getFieldName());
    }

    @Test
    @DisplayName("Should handle camelCase field names")
    void shouldHandleCamelCaseFieldNames() {
        var error = new MatchingError("User", "currentPassword", null);

        assertEquals("'User' failed to match the currentPassword' field.", error.getMessage());
        assertEquals("currentPassword", error.getFieldName());
    }

    @Test
    @DisplayName("Should handle snake_case field names")
    void shouldHandleSnakeCaseFieldNames() {
        var error = new MatchingError("User", "current_password", null);

        assertEquals("'User' failed to match the current_password' field.", error.getMessage());
        assertEquals("current_password", error.getFieldName());
    }

    @Test
    @DisplayName("Should create exception for different matching scenarios")
    void shouldCreateExceptionForDifferentMatchingScenarios() {
        var passwordError = new MatchingError("User", "password", null);
        var emailError = new MatchingError("Account", "recoveryEmail", null);
        var tokenError = new MatchingError("Session", "csrfToken", null);
        var hashError = new MatchingError("File", "checksum", null);

        assertEquals("'User' failed to match the password' field.", passwordError.getMessage());
        assertEquals("'Account' failed to match the recoveryEmail' field.", emailError.getMessage());
        assertEquals("'Session' failed to match the csrfToken' field.", tokenError.getMessage());
        assertEquals("'File' failed to match the checksum' field.", hashError.getMessage());
    }

}