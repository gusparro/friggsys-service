package com.gusparro.friggsys.usecase.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UseCaseException Tests")
class UseCaseExceptionTest {

    @Test
    @DisplayName("Should create exception with message only")
    void shouldCreateExceptionWithMessageOnly() {
        var exception = new TestUseCaseException("Test message");

        assertEquals("Test message", exception.getMessage());
        assertNotNull(exception.getDetails());
        assertTrue(exception.getDetails().isEmpty());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Should create exception with message and cause")
    void shouldCreateExceptionWithMessageAndCause() {
        var cause = new RuntimeException("Cause message");
        var exception = new TestUseCaseException("Test message", cause);

        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertNotNull(exception.getDetails());
        assertTrue(exception.getDetails().isEmpty());
    }

    @Test
    @DisplayName("Should create exception with message and details")
    void shouldCreateExceptionWithMessageAndDetails() {
        Map<String, Object> details = new HashMap<>();
        details.put("key1", "value1");
        details.put("key2", 123);
        details.put("key3", true);

        var exception = new TestUseCaseException("Test message", details);

        assertEquals("Test message", exception.getMessage());
        assertEquals(3, exception.getDetails().size());
        assertEquals("value1", exception.getDetails().get("key1"));
        assertEquals(123, exception.getDetails().get("key2"));
        assertEquals(true, exception.getDetails().get("key3"));
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Should create exception with message, details and cause")
    void shouldCreateExceptionWithMessageDetailsAndCause() {
        Map<String, Object> details = new HashMap<>();
        details.put("errorCode", "ERR001");
        details.put("severity", "HIGH");

        var cause = new IllegalArgumentException("Invalid argument");
        var exception = new TestUseCaseException("Test message", details, cause);

        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(2, exception.getDetails().size());
        assertEquals("ERR001", exception.getDetails().get("errorCode"));
        assertEquals("HIGH", exception.getDetails().get("severity"));
    }

    @Test
    @DisplayName("Should handle null details map in constructor with details only")
    void shouldHandleNullDetailsMapInConstructorWithDetailsOnly() {
        var exception = new TestUseCaseException("Test message", (Map<String, Object>) null);

        assertNotNull(exception.getDetails());
        assertTrue(exception.getDetails().isEmpty());
    }

    @Test
    @DisplayName("Should handle null details map in constructor with details and cause")
    void shouldHandleNullDetailsMapInConstructorWithDetailsAndCause() {
        var cause = new RuntimeException("Cause");
        var exception = new TestUseCaseException("Test message", null, cause);

        assertNotNull(exception.getDetails());
        assertTrue(exception.getDetails().isEmpty());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Should add detail to exception")
    void shouldAddDetailToException() {
        var exception = new TestUseCaseException("Test message");

        exception.addDetail("newKey", "newValue");

        assertEquals(1, exception.getDetails().size());
        assertEquals("newValue", exception.getDetails().get("newKey"));
    }

    @Test
    @DisplayName("Should add multiple details to exception")
    void shouldAddMultipleDetailsToException() {
        var exception = new TestUseCaseException("Test message");

        exception.addDetail("key1", "value1");
        exception.addDetail("key2", 42);
        exception.addDetail("key3", true);
        exception.addDetail("key4", 3.14);

        assertEquals(4, exception.getDetails().size());
        assertEquals("value1", exception.getDetails().get("key1"));
        assertEquals(42, exception.getDetails().get("key2"));
        assertEquals(true, exception.getDetails().get("key3"));
        assertEquals(3.14, exception.getDetails().get("key4"));
    }

    @Test
    @DisplayName("Should update existing detail when adding with same key")
    void shouldUpdateExistingDetailWhenAddingWithSameKey() {
        Map<String, Object> details = new HashMap<>();
        details.put("status", "pending");

        var exception = new TestUseCaseException("Test message", details);

        assertEquals("pending", exception.getDetails().get("status"));

        exception.addDetail("status", "completed");

        assertEquals(1, exception.getDetails().size());
        assertEquals("completed", exception.getDetails().get("status"));
    }

    @Test
    @DisplayName("Should add detail to exception created with existing details")
    void shouldAddDetailToExceptionCreatedWithExistingDetails() {
        Map<String, Object> initialDetails = new HashMap<>();
        initialDetails.put("initialKey", "initialValue");

        var exception = new TestUseCaseException("Test message", initialDetails);

        exception.addDetail("additionalKey", "additionalValue");

        assertEquals(2, exception.getDetails().size());
        assertEquals("initialValue", exception.getDetails().get("initialKey"));
        assertEquals("additionalValue", exception.getDetails().get("additionalKey"));
    }

    @Test
    @DisplayName("Should handle null value when adding detail")
    void shouldHandleNullValueWhenAddingDetail() {
        var exception = new TestUseCaseException("Test message");

        exception.addDetail("nullKey", null);

        assertEquals(1, exception.getDetails().size());
        assertTrue(exception.getDetails().containsKey("nullKey"));
        assertNull(exception.getDetails().get("nullKey"));
    }

    @Test
    @DisplayName("Should handle complex objects as detail values")
    void shouldHandleComplexObjectsAsDetailValues() {
        var exception = new TestUseCaseException("Test message");

        Map<String, String> complexObject = Map.of("nested", "value");
        exception.addDetail("complex", complexObject);

        assertEquals(1, exception.getDetails().size());
        assertEquals(complexObject, exception.getDetails().get("complex"));
    }

    @Test
    @DisplayName("Should maintain details across exception chain")
    void shouldMaintainDetailsAcrossExceptionChain() {
        Map<String, Object> details = Map.of("operation", "save", "userId", "123");
        var cause = new RuntimeException("Database error");
        var exception = new TestUseCaseException("UseCase failed", details, cause);

        assertEquals("UseCase failed", exception.getMessage());
        assertEquals("Database error", exception.getCause().getMessage());
        assertEquals(2, exception.getDetails().size());
        assertEquals("save", exception.getDetails().get("operation"));
        assertEquals("123", exception.getDetails().get("userId"));
    }

    @Test
    @DisplayName("Should be throwable as RuntimeException")
    void shouldBeThrowableAsRuntimeException() {
        assertThrows(TestUseCaseException.class, () -> {
            throw new TestUseCaseException("Test exception");
        });
    }

    @Test
    @DisplayName("Should allow details map to be modified after creation")
    void shouldAllowDetailsMapToBeModifiedAfterCreation() {
        var exception = new TestUseCaseException("Test message");

        exception.getDetails().put("directKey", "directValue");

        assertEquals(1, exception.getDetails().size());
        assertEquals("directValue", exception.getDetails().get("directKey"));
    }

    private static class TestUseCaseException extends UseCaseException {
        public TestUseCaseException(String message) {
            super(message);
        }

        public TestUseCaseException(String message, Throwable cause) {
            super(message, cause);
        }

        public TestUseCaseException(String message, Map<String, Object> details) {
            super(message, details);
        }

        public TestUseCaseException(String message, Map<String, Object> details, Throwable cause) {
            super(message, details, cause);
        }
    }

}