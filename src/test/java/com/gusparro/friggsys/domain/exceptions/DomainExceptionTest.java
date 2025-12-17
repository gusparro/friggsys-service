package com.gusparro.friggsys.domain.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DomainException Tests")
public class DomainExceptionTest {

    @Test
    @DisplayName("Should create exception with message only")
    void shouldCreateExceptionWithMessageOnly() {
        var exception = new TestDomainException("Test message");

        assertEquals("Test message", exception.getMessage());
        assertNotNull(exception.getDetails());
        assertTrue(exception.getDetails().isEmpty());
    }

    @Test
    @DisplayName("Should create exception with message and cause")
    void shouldCreateExceptionWithMessageAndCause() {
        var cause = new RuntimeException("Cause message");
        var exception = new TestDomainException("Test message", cause);

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

        var exception = new TestDomainException("Test message", details);

        assertEquals("Test message", exception.getMessage());
        assertEquals(2, exception.getDetails().size());
        assertEquals("value1", exception.getDetails().get("key1"));
        assertEquals(123, exception.getDetails().get("key2"));
    }

    @Test
    @DisplayName("Should create exception with message, null details and cause")
    void shouldCreateExceptionWithMessageNullDetailsAndCause() {
        var cause = new RuntimeException("Cause");
        var exception = new TestDomainException("Test message", null, cause);

        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Should create exception with message, details and cause")
    void shouldCreateExceptionWithMessageDetailsAndCause() {
        Map<String, Object> details = Map.of("key", "value");
        var cause = new RuntimeException("Cause");
        var exception = new TestDomainException("Test message", details, cause);

        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(1, exception.getDetails().size());
        assertEquals("value", exception.getDetails().get("key"));
    }

    @Test
    @DisplayName("Should handle null details map")
    void shouldHandleNullDetailsMap() {
        var exception = new TestDomainException("Test message", (Map<String, Object>) null);

        assertNotNull(exception.getDetails());
        assertTrue(exception.getDetails().isEmpty());
    }

    @Test
    @DisplayName("Should add detail to exception")
    void shouldAddDetailToException() {
        var exception = new TestDomainException("Test message");

        exception.addDetail("newKey", "newValue");

        assertEquals(1, exception.getDetails().size());
        assertEquals("newValue", exception.getDetails().get("newKey"));
    }

    @Test
    @DisplayName("Should add multiple details to exception")
    void shouldAddMultipleDetailsToException() {
        var exception = new TestDomainException("Test message");

        exception.addDetail("key1", "value1");
        exception.addDetail("key2", 42);
        exception.addDetail("key3", true);

        assertEquals(3, exception.getDetails().size());
        assertEquals("value1", exception.getDetails().get("key1"));
        assertEquals(42, exception.getDetails().get("key2"));
        assertEquals(true, exception.getDetails().get("key3"));
    }

    private static class TestDomainException extends DomainException {
        public TestDomainException(String message) {
            super(message);
        }

        public TestDomainException(String message, Throwable cause) {
            super(message, cause);
        }

        public TestDomainException(String message, Map<String, Object> details) {
            super(message, details);
        }

        public TestDomainException(String message, Map<String, Object> details, Throwable cause) {
            super(message, details, cause);
        }
    }

}
