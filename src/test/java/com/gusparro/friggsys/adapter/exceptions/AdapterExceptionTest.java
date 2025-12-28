package com.gusparro.friggsys.adapter.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AdapterException Tests")
class AdapterExceptionTest {

    @Test
    @DisplayName("Should create exception with message only")
    void shouldCreateExceptionWithMessageOnly() {
        var exception = new TestAdapterException("Test message");

        assertEquals("Test message", exception.getMessage());
        assertNotNull(exception.getDetails());
        assertTrue(exception.getDetails().isEmpty());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Should create exception with message and cause")
    void shouldCreateExceptionWithMessageAndCause() {
        var cause = new RuntimeException("Cause message");
        var exception = new TestAdapterException("Test message", cause);

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

        var exception = new TestAdapterException("Test message", details);

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
        var exception = new TestAdapterException("Test message", details, cause);

        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(2, exception.getDetails().size());
        assertEquals("ERR001", exception.getDetails().get("errorCode"));
        assertEquals("HIGH", exception.getDetails().get("severity"));
    }

    @Test
    @DisplayName("Should handle null details map in constructor with details only")
    void shouldHandleNullDetailsMapInConstructorWithDetailsOnly() {
        var exception = new TestAdapterException("Test message", (Map<String, Object>) null);

        assertNotNull(exception.getDetails());
        assertTrue(exception.getDetails().isEmpty());
    }

    @Test
    @DisplayName("Should handle null details map in constructor with details and cause")
    void shouldHandleNullDetailsMapInConstructorWithDetailsAndCause() {
        var cause = new RuntimeException("Cause");
        var exception = new TestAdapterException("Test message", null, cause);

        assertNotNull(exception.getDetails());
        assertTrue(exception.getDetails().isEmpty());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Should add detail to exception")
    void shouldAddDetailToException() {
        var exception = new TestAdapterException("Test message");

        exception.addDetail("newKey", "newValue");

        assertEquals(1, exception.getDetails().size());
        assertEquals("newValue", exception.getDetails().get("newKey"));
    }

    @Test
    @DisplayName("Should add multiple details to exception")
    void shouldAddMultipleDetailsToException() {
        var exception = new TestAdapterException("Test message");

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

        var exception = new TestAdapterException("Test message", details);

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

        var exception = new TestAdapterException("Test message", initialDetails);

        exception.addDetail("additionalKey", "additionalValue");

        assertEquals(2, exception.getDetails().size());
        assertEquals("initialValue", exception.getDetails().get("initialKey"));
        assertEquals("additionalValue", exception.getDetails().get("additionalKey"));
    }

    @Test
    @DisplayName("Should handle null value when adding detail")
    void shouldHandleNullValueWhenAddingDetail() {
        var exception = new TestAdapterException("Test message");

        exception.addDetail("nullKey", null);

        assertEquals(1, exception.getDetails().size());
        assertTrue(exception.getDetails().containsKey("nullKey"));
        assertNull(exception.getDetails().get("nullKey"));
    }

    @Test
    @DisplayName("Should handle complex objects as detail values")
    void shouldHandleComplexObjectsAsDetailValues() {
        var exception = new TestAdapterException("Test message");

        Map<String, String> complexObject = Map.of("nested", "value");
        exception.addDetail("complex", complexObject);

        assertEquals(1, exception.getDetails().size());
        assertEquals(complexObject, exception.getDetails().get("complex"));
    }

    @Test
    @DisplayName("Should maintain details across exception chain")
    void shouldMaintainDetailsAcrossExceptionChain() {
        Map<String, Object> details = Map.of("operation", "save", "resourceId", "123");
        var cause = new RuntimeException("Database error");
        var exception = new TestAdapterException("Adapter failed", details, cause);

        assertEquals("Adapter failed", exception.getMessage());
        assertEquals("Database error", exception.getCause().getMessage());
        assertEquals(2, exception.getDetails().size());
        assertEquals("save", exception.getDetails().get("operation"));
        assertEquals("123", exception.getDetails().get("resourceId"));
    }

    @Test
    @DisplayName("Should be throwable as RuntimeException")
    void shouldBeThrowableAsRuntimeException() {
        assertThrows(TestAdapterException.class, () -> {
            throw new TestAdapterException("Test exception");
        });
    }

    @Test
    @DisplayName("Should allow details map to be modified after creation")
    void shouldAllowDetailsMapToBeModifiedAfterCreation() {
        var exception = new TestAdapterException("Test message");

        exception.getDetails().put("directKey", "directValue");

        assertEquals(1, exception.getDetails().size());
        assertEquals("directValue", exception.getDetails().get("directKey"));
    }

    @Test
    @DisplayName("Should extend RuntimeException")
    void shouldExtendRuntimeException() {
        var exception = new TestAdapterException("Test");

        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    @DisplayName("Should create exception with empty message")
    void shouldCreateExceptionWithEmptyMessage() {
        var exception = new TestAdapterException("");

        assertEquals("", exception.getMessage());
        assertNotNull(exception.getDetails());
    }

    @Test
    @DisplayName("Should create exception with null message")
    void shouldCreateExceptionWithNullMessage() {
        var exception = new TestAdapterException(null);

        assertNull(exception.getMessage());
        assertNotNull(exception.getDetails());
    }

    @Test
    @DisplayName("Should handle multiple nested causes")
    void shouldHandleMultipleNestedCauses() {
        var rootCause = new IllegalArgumentException("Root cause");
        var intermediateCause = new RuntimeException("Intermediate", rootCause);
        var exception = new TestAdapterException("Adapter error", intermediateCause);

        assertEquals("Adapter error", exception.getMessage());
        assertEquals(intermediateCause, exception.getCause());
        assertEquals(rootCause, exception.getCause().getCause());
    }

    @Test
    @DisplayName("Should support different detail value types")
    void shouldSupportDifferentDetailValueTypes() {
        var exception = new TestAdapterException("Test");

        exception.addDetail("string", "text");
        exception.addDetail("integer", 42);
        exception.addDetail("long", 100L);
        exception.addDetail("double", 3.14);
        exception.addDetail("boolean", true);
        exception.addDetail("object", Map.of("key", "value"));

        assertEquals(6, exception.getDetails().size());
        assertInstanceOf(String.class, exception.getDetails().get("string"));
        assertInstanceOf(Integer.class, exception.getDetails().get("integer"));
        assertInstanceOf(Long.class, exception.getDetails().get("long"));
        assertInstanceOf(Double.class, exception.getDetails().get("double"));
        assertInstanceOf(Boolean.class, exception.getDetails().get("boolean"));
        assertInstanceOf(Map.class, exception.getDetails().get("object"));
    }

    @Test
    @DisplayName("Should allow catching as RuntimeException")
    void shouldAllowCatchingAsRuntimeException() {
        try {
            throw new TestAdapterException("Error");
        } catch (RuntimeException e) {
            assertInstanceOf(TestAdapterException.class, e);
            assertEquals("Error", e.getMessage());
        }
    }

    @Test
    @DisplayName("Should preserve details when exception is rethrown")
    void shouldPreserveDetailsWhenExceptionIsRethrown() {
        var exception = new TestAdapterException("Original");
        exception.addDetail("key", "value");

        try {
            throw exception;
        } catch (TestAdapterException e) {
            assertEquals("Original", e.getMessage());
            assertEquals(1, e.getDetails().size());
            assertEquals("value", e.getDetails().get("key"));
        }
    }

    private static class TestAdapterException extends AdapterException {
        public TestAdapterException(String message) {
            super(message);
        }

        public TestAdapterException(String message, Throwable cause) {
            super(message, cause);
        }

        public TestAdapterException(String message, Map<String, Object> details) {
            super(message, details);
        }

        public TestAdapterException(String message, Map<String, Object> details, Throwable cause) {
            super(message, details, cause);
        }
    }

}