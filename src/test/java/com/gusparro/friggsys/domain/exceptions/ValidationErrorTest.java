package com.gusparro.friggsys.domain.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValidationError Tests")
public class ValidationErrorTest {

    @Test
    @DisplayName("Should create validation error with message only")
    void shouldCreateValidationErrorWithMessageOnly() {
        var error = new ValidationError("Invalid value");

        assertEquals("Invalid value", error.getMessage());
        assertNull(error.getField());
        assertNotNull(error.getDetails());
        assertTrue(error.getDetails().isEmpty());
    }

    @Test
    @DisplayName("Should create validation error with message and field")
    void shouldCreateValidationErrorWithMessageAndField() {
        var error = new ValidationError("Invalid email format", "email");

        assertEquals("Invalid email format", error.getMessage());
        assertEquals("email", error.getField());
        assertNotNull(error.getDetails());
        assertTrue(error.getDetails().isEmpty());
    }

    @Test
    @DisplayName("Should create validation error with message, field and details")
    void shouldCreateValidationErrorWithMessageFieldAndDetails() {
        Map<String, Object> details = Map.of(
                "pattern", "^[a-z]+$",
                "actualValue", "ABC123"
        );

        var error = new ValidationError("Pattern mismatch", "username", details);

        assertEquals("Pattern mismatch", error.getMessage());
        assertEquals("username", error.getField());
        assertEquals(2, error.getDetails().size());
        assertEquals("^[a-z]+$", error.getDetails().get("pattern"));
        assertEquals("ABC123", error.getDetails().get("actualValue"));
    }

    @Test
    @DisplayName("Should handle null field")
    void shouldHandleNullField() {
        var error = new ValidationError("Error message", null, null);

        assertEquals("Error message", error.getMessage());
        assertNull(error.getField());
        assertNotNull(error.getDetails());
    }

}
