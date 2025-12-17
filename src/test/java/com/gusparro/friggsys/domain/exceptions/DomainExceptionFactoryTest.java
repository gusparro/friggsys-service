package com.gusparro.friggsys.domain.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DomainExceptionFactory Tests")
class DomainExceptionFactoryTest {

    @Test
    @DisplayName("Should create empty field validation error")
    void shouldCreateEmptyFieldValidationError() {
        var error = DomainExceptionFactory.emptyField("name");

        assertEquals("name cannot be empty", error.getMessage());
        assertEquals("name", error.getField());
        assertEquals("empty_check", error.getDetails().get("validationType"));
        assertNotNull(error.getDetails().get("timestamp"));
    }

    @Test
    @DisplayName("Should create min length validation error")
    void shouldCreateMinLengthValidationError() {
        var error = DomainExceptionFactory.minLength("password", 8, 5);

        assertEquals("password must have at least 8 characters", error.getMessage());
        assertEquals("password", error.getField());
        assertEquals("min_length", error.getDetails().get("validationType"));
        assertEquals(8, error.getDetails().get("minLength"));
        assertEquals(5, error.getDetails().get("actualLength"));
        assertEquals(3, error.getDetails().get("missingCharacters"));
        assertNotNull(error.getDetails().get("timestamp"));
    }

    @Test
    @DisplayName("Should create max length validation error")
    void shouldCreateMaxLengthValidationError() {
        var error = DomainExceptionFactory.maxLength("description", 100, 150);

        assertEquals("description cannot exceed 100 characters", error.getMessage());
        assertEquals("description", error.getField());
        assertEquals("max_length", error.getDetails().get("validationType"));
        assertEquals(100, error.getDetails().get("maxLength"));
        assertEquals(150, error.getDetails().get("actualLength"));
        assertEquals(50, error.getDetails().get("excessCharacters"));
        assertNotNull(error.getDetails().get("timestamp"));
    }

    @Test
    @DisplayName("Should create invalid pattern validation error")
    void shouldCreateInvalidPatternValidationError() {
        var error = DomainExceptionFactory.invalidPattern("email", "^[a-z]+@[a-z]+\\.[a-z]+$",
                "lowercase letters only");

        assertEquals("email does not match required pattern", error.getMessage());
        assertEquals("email", error.getField());
        assertEquals("pattern_mismatch", error.getDetails().get("validationType"));
        assertEquals("^[a-z]+@[a-z]+\\.[a-z]+$", error.getDetails().get("pattern"));
        assertEquals("lowercase letters only", error.getDetails().get("requirement"));
        assertNotNull(error.getDetails().get("timestamp"));
    }

    @Test
    @DisplayName("Should create generic invalid validation error")
    void shouldCreateGenericInvalidValidationError() {
        var error = DomainExceptionFactory.invalid("age", "Age must be between 18 and 100");

        assertEquals("Age must be between 18 and 100", error.getMessage());
        assertEquals("age", error.getField());
        assertEquals("generic", error.getDetails().get("validationType"));
        assertNotNull(error.getDetails().get("timestamp"));
    }

    @Test
    @DisplayName("Should create invalid state error without entity ID")
    void shouldCreateInvalidStateErrorWithoutEntityId() {
        var error = DomainExceptionFactory.invalidState("Order", "cancelled", "ship");

        assertEquals("It is not possible to execute 'ship' on Order in the 'cancelled' state",
                error.getMessage());
        assertEquals("Order", error.getEntityName());
        assertEquals("cancelled", error.getCurrentState());
        assertEquals("ship", error.getAction());
        assertNotNull(error.getDetails().get("timestamp"));
        assertFalse(error.getDetails().containsKey("entityId"));
    }

    @Test
    @DisplayName("Should create invalid state error with entity ID")
    void shouldCreateInvalidStateErrorWithEntityId() {
        var entityId = UUID.randomUUID();
        var error = DomainExceptionFactory.invalidState("User", entityId, "blocked", "login");

        assertEquals("It is not possible to execute 'login' on User in the 'blocked' state",
                error.getMessage());
        assertEquals("User", error.getEntityName());
        assertEquals("blocked", error.getCurrentState());
        assertEquals("login", error.getAction());
        assertNotNull(error.getDetails().get("timestamp"));
        assertEquals(entityId.toString(), error.getDetails().get("entityId"));
    }

    @Test
    @DisplayName("Should create unique empty field errors for different fields")
    void shouldCreateUniqueEmptyFieldErrorsForDifferentFields() {
        var error1 = DomainExceptionFactory.emptyField("firstName");
        var error2 = DomainExceptionFactory.emptyField("lastName");

        assertNotEquals(error1.getMessage(), error2.getMessage());
        assertEquals("firstName", error1.getField());
        assertEquals("lastName", error2.getField());
    }

    @Test
    @DisplayName("Should calculate missing characters correctly")
    void shouldCalculateMissingCharactersCorrectly() {
        var error = DomainExceptionFactory.minLength("username", 10, 3);

        assertEquals(7, error.getDetails().get("missingCharacters"));
    }

    @Test
    @DisplayName("Should calculate excess characters correctly")
    void shouldCalculateExcessCharactersCorrectly() {
        var error = DomainExceptionFactory.maxLength("bio", 200, 250);

        assertEquals(50, error.getDetails().get("excessCharacters"));
    }

}
