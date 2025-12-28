package com.gusparro.friggsys.adapter.exceptions.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProblemDetails Tests")
class ProblemDetailsTest {

    @Test
    @DisplayName("Should create ProblemDetails with all fields")
    void shouldCreateProblemDetailsWithAllFields() {
        var problemType = ProblemType.IS_CONFLICT_ERROR;
        var message = "Resource already exists";
        var instance = "/api/users/123";
        var fields = List.of(
                FieldValidationDetail.builder()
                        .name("email")
                        .message("Email already in use")
                        .build()
        );
        Map<String, Object> additionalProperties = Map.of("errorCode", "DUPLICATE_EMAIL", "timestamp", "2025-01-01T10:00:00Z");

        var problemDetails = ProblemDetails.buildBodyResponse(
                problemType,
                message,
                instance,
                fields,
                additionalProperties
        );

        assertNotNull(problemDetails);
        assertEquals(problemType.getTitle(), problemDetails.title());
        assertEquals(problemType.getStatus(), problemDetails.status());
        assertEquals(message, problemDetails.detail());
        assertEquals(instance, problemDetails.instance());
        assertEquals(1, problemDetails.fields().size());
        assertEquals(2, problemDetails.additionalProperties().size());
    }

    @Test
    @DisplayName("Should create ProblemDetails with null fields")
    void shouldCreateProblemDetailsWithNullFields() {
        var problemType = ProblemType.IS_BAD_REQUEST_ERROR;
        var message = "Invalid request";
        var instance = "/api/orders";

        var problemDetails = ProblemDetails.buildBodyResponse(
                problemType,
                message,
                instance,
                null,
                null
        );

        assertNotNull(problemDetails);
        assertEquals(problemType.getTitle(), problemDetails.title());
        assertEquals(problemType.getStatus(), problemDetails.status());
        assertEquals(message, problemDetails.detail());
        assertEquals(instance, problemDetails.instance());
        assertNull(problemDetails.fields());
        assertNull(problemDetails.additionalProperties());
    }

    @Test
    @DisplayName("Should create ProblemDetails with empty fields list")
    void shouldCreateProblemDetailsWithEmptyFieldsList() {
        var problemType = ProblemType.IS_NOT_FOUND_ERROR;
        var message = "Resource not found";
        var instance = "/api/products/999";
        var fields = new ArrayList<FieldValidationDetail>();

        var problemDetails = ProblemDetails.buildBodyResponse(
                problemType,
                message,
                instance,
                fields,
                null
        );

        assertNotNull(problemDetails);
        assertNotNull(problemDetails.fields());
        assertTrue(problemDetails.fields().isEmpty());
    }

    @Test
    @DisplayName("Should create ProblemDetails with empty additional properties")
    void shouldCreateProblemDetailsWithEmptyAdditionalProperties() {
        var problemType = ProblemType.IS_INTERNAL_SERVER_ERROR;
        var message = "Internal server error occurred";
        var instance = "/api/payments";
        var additionalProperties = new HashMap<String, Object>();

        var problemDetails = ProblemDetails.buildBodyResponse(
                problemType,
                message,
                instance,
                null,
                additionalProperties
        );

        assertNotNull(problemDetails);
        assertNotNull(problemDetails.additionalProperties());
        assertTrue(problemDetails.additionalProperties().isEmpty());
    }

    @Test
    @DisplayName("Should create ProblemDetails with multiple field validation details")
    void shouldCreateProblemDetailsWithMultipleFieldValidationDetails() {
        var problemType = ProblemType.IS_BAD_REQUEST_ERROR;
        var message = "Validation failed";
        var instance = "/api/users";
        var fields = List.of(
                FieldValidationDetail.builder()
                        .name("username")
                        .message("Username is required")
                        .build(),
                FieldValidationDetail.builder()
                        .name("password")
                        .message("Password must be at least 8 characters")
                        .build(),
                FieldValidationDetail.builder()
                        .name("email")
                        .message("Invalid email format")
                        .build()
        );

        var problemDetails = ProblemDetails.buildBodyResponse(
                problemType,
                message,
                instance,
                fields,
                null
        );

        assertNotNull(problemDetails);
        assertEquals(3, problemDetails.fields().size());
        assertEquals("username", problemDetails.fields().get(0).name());
        assertEquals("password", problemDetails.fields().get(1).name());
        assertEquals("email", problemDetails.fields().get(2).name());
    }

    @Test
    @DisplayName("Should create ProblemDetails with multiple additional properties")
    void shouldCreateProblemDetailsWithMultipleAdditionalProperties() {
        var problemType = ProblemType.IS_CONFLICT_ERROR;
        var message = "Conflict detected";
        var instance = "/api/orders/456";
        Map<String, Object> additionalProperties = Map.of(
                "conflictType", "duplicate_order",
                "entityId", "456",
                "timestamp", "2025-01-01T12:00:00Z",
                "retryable", true,
                "attemptCount", 3
        );

        var problemDetails = ProblemDetails.buildBodyResponse(
                problemType,
                message,
                instance,
                null,
                additionalProperties
        );

        assertNotNull(problemDetails);
        assertEquals(5, problemDetails.additionalProperties().size());
        assertEquals("duplicate_order", problemDetails.additionalProperties().get("conflictType"));
        assertEquals("456", problemDetails.additionalProperties().get("entityId"));
        assertEquals(true, problemDetails.additionalProperties().get("retryable"));
        assertEquals(3, problemDetails.additionalProperties().get("attemptCount"));
    }

    @Test
    @DisplayName("Should create ProblemDetails using builder directly")
    void shouldCreateProblemDetailsUsingBuilderDirectly() {
        var problemDetails = ProblemDetails.builder()
                .title("Bad Request")
                .status(400)
                .detail("Invalid input")
                .instance("/api/test")
                .fields(null)
                .additionalProperties(null)
                .build();

        assertNotNull(problemDetails);
        assertEquals("Bad Request", problemDetails.title());
        assertEquals(400, problemDetails.status());
        assertEquals("Invalid input", problemDetails.detail());
        assertEquals("/api/test", problemDetails.instance());
        assertNull(problemDetails.fields());
        assertNull(problemDetails.additionalProperties());
    }

    @Test
    @DisplayName("Should preserve problem type title and status")
    void shouldPreserveProblemTypeTitleAndStatus() {
        var notFoundType = ProblemType.IS_NOT_FOUND_ERROR;
        var conflictType = ProblemType.IS_CONFLICT_ERROR;
        var badRequestType = ProblemType.IS_BAD_REQUEST_ERROR;

        var notFoundDetails = ProblemDetails.buildBodyResponse(notFoundType, "Not found", "/api/test", null, null);
        var conflictDetails = ProblemDetails.buildBodyResponse(conflictType, "Conflict", "/api/test", null, null);
        var badRequestDetails = ProblemDetails.buildBodyResponse(badRequestType, "Bad request", "/api/test", null, null);

        assertEquals(notFoundType.getTitle() , notFoundDetails.title());
        assertEquals(notFoundType.getStatus(), notFoundDetails.status());

        assertEquals(conflictType.getTitle(), conflictDetails.title());
        assertEquals(conflictType.getStatus(), conflictDetails.status());

        assertEquals(badRequestType.getTitle(), badRequestDetails.title());
        assertEquals(badRequestType.getStatus(), badRequestDetails.status());
    }

    @Test
    @DisplayName("Should handle different instance paths")
    void shouldHandleDifferentInstancePaths() {
        var problemType = ProblemType.IS_NOT_FOUND_ERROR;
        var message = "Resource not found";

        var details1 = ProblemDetails.buildBodyResponse(problemType, message, "/api/users/123", null, null);
        var details2 = ProblemDetails.buildBodyResponse(problemType, message, "/api/orders/abc-def", null, null);
        var details3 = ProblemDetails.buildBodyResponse(problemType, message, "/v2/products/search", null, null);

        assertEquals("/api/users/123", details1.instance());
        assertEquals("/api/orders/abc-def", details2.instance());
        assertEquals("/v2/products/search", details3.instance());
    }

    @Test
    @DisplayName("Should handle different detail messages")
    void shouldHandleDifferentDetailMessages() {
        var problemType = ProblemType.IS_BAD_REQUEST_ERROR;
        var instance = "/api/test";

        var details1 = ProblemDetails.buildBodyResponse(problemType, "Invalid email format", instance, null, null);
        var details2 = ProblemDetails.buildBodyResponse(problemType, "Password too short", instance, null, null);
        var details3 = ProblemDetails.buildBodyResponse(problemType, "Username already exists", instance, null, null);

        assertEquals("Invalid email format", details1.detail());
        assertEquals("Password too short", details2.detail());
        assertEquals("Username already exists", details3.detail());
    }

    @Test
    @DisplayName("Should create ProblemDetails with complex additional properties")
    void shouldCreateProblemDetailsWithComplexAdditionalProperties() {
        var problemType = ProblemType.IS_INTERNAL_SERVER_ERROR;
        var message = "Database connection failed";
        var instance = "/api/transactions";
        var additionalProperties = Map.of(
                "errorCode", "DB_CONNECTION_ERROR",
                "databaseName", "friggsys_prod",
                "connectionTimeout", 30000,
                "retryAttempts", List.of(1, 2, 3),
                "metadata", Map.of("host", "localhost", "port", 5432)
        );

        var problemDetails = ProblemDetails.buildBodyResponse(
                problemType,
                message,
                instance,
                null,
                additionalProperties
        );

        assertNotNull(problemDetails);
        assertEquals(5, problemDetails.additionalProperties().size());
        assertEquals("DB_CONNECTION_ERROR", problemDetails.additionalProperties().get("errorCode"));
        assertEquals(30000, problemDetails.additionalProperties().get("connectionTimeout"));
        assertInstanceOf(List.class, problemDetails.additionalProperties().get("retryAttempts"));
        assertInstanceOf(Map.class, problemDetails.additionalProperties().get("metadata"));
    }

    @Test
    @DisplayName("Should create ProblemDetails with field validation containing all details")
    void shouldCreateProblemDetailsWithFieldValidationContainingAllDetails() {
        var problemType = ProblemType.IS_BAD_REQUEST_ERROR;
        var message = "Validation error";
        var instance = "/api/accounts";
        var field = FieldValidationDetail.builder()
                .name("accountNumber")
                .message("Account number must be numeric")
                .build();

        var problemDetails = ProblemDetails.buildBodyResponse(
                problemType,
                message,
                instance,
                List.of(field),
                null
        );

        assertNotNull(problemDetails);
        assertNotNull(problemDetails.fields());
        assertEquals(1, problemDetails.fields().size());
        assertEquals("accountNumber", problemDetails.fields().get(0).name());
        assertEquals("Account number must be numeric", problemDetails.fields().get(0).message());
    }

    @Test
    @DisplayName("Should handle null message in detail")
    void shouldHandleNullMessageInDetail() {
        var problemType = ProblemType.IS_INTERNAL_SERVER_ERROR;
        var instance = "/api/error";

        var problemDetails = ProblemDetails.buildBodyResponse(
                problemType,
                null,
                instance,
                null,
                null
        );

        assertNotNull(problemDetails);
        assertNull(problemDetails.detail());
    }

    @Test
    @DisplayName("Should handle null instance")
    void shouldHandleNullInstance() {
        var problemType = ProblemType.IS_BAD_REQUEST_ERROR;
        var message = "Invalid request";

        var problemDetails = ProblemDetails.buildBodyResponse(
                problemType,
                message,
                null,
                null,
                null
        );

        assertNotNull(problemDetails);
        assertNull(problemDetails.instance());
    }

    @Test
    @DisplayName("Should create different ProblemDetails for different problem types")
    void shouldCreateDifferentProblemDetailsForDifferentProblemTypes() {
        var instance = "/api/test";
        var message = "Error occurred";

        var badRequest = ProblemDetails.buildBodyResponse(ProblemType.IS_BAD_REQUEST_ERROR, message, instance, null, null);
        var notFound = ProblemDetails.buildBodyResponse(ProblemType.IS_NOT_FOUND_ERROR, message, instance, null, null);
        var conflict = ProblemDetails.buildBodyResponse(ProblemType.IS_CONFLICT_ERROR, message, instance, null, null);
        var serverError = ProblemDetails.buildBodyResponse(ProblemType.IS_INTERNAL_SERVER_ERROR, message, instance, null, null);

        assertNotEquals(badRequest.status(), notFound.status());
        assertNotEquals(badRequest.status(), conflict.status());
        assertNotEquals(badRequest.status(), serverError.status());
        assertNotEquals(notFound.status(), conflict.status());
    }

    @Test
    @DisplayName("Should preserve all field validation details order")
    void shouldPreserveAllFieldValidationDetailsOrder() {
        var problemType = ProblemType.IS_BAD_REQUEST_ERROR;
        var message = "Multiple validation errors";
        var instance = "/api/registration";
        var fields = List.of(
                FieldValidationDetail.builder().name("field1").message("error1").build(),
                FieldValidationDetail.builder().name("field2").message("error2").build(),
                FieldValidationDetail.builder().name("field3").message("error3").build()
        );

        var problemDetails = ProblemDetails.buildBodyResponse(
                problemType,
                message,
                instance,
                fields,
                null
        );

        assertEquals("field1", problemDetails.fields().get(0).name());
        assertEquals("field2", problemDetails.fields().get(1).name());
        assertEquals("field3", problemDetails.fields().get(2).name());
        assertEquals("error1", problemDetails.fields().get(0).message());
        assertEquals("error2", problemDetails.fields().get(1).message());
        assertEquals("error3", problemDetails.fields().get(2).message());
    }

    @Test
    @DisplayName("Should handle additional properties with null values")
    void shouldHandleAdditionalPropertiesWithNullValues() {
        var problemType = ProblemType.IS_CONFLICT_ERROR;
        var message = "Conflict";
        var instance = "/api/test";
        Map<String, Object> additionalProperties = new HashMap<>();
        additionalProperties.put("key1", "value1");
        additionalProperties.put("key2", null);
        additionalProperties.put("key3", "value3");

        var problemDetails = ProblemDetails.buildBodyResponse(
                problemType,
                message,
                instance,
                null,
                additionalProperties
        );

        assertNotNull(problemDetails);
        assertEquals(3, problemDetails.additionalProperties().size());
        assertEquals("value1", problemDetails.additionalProperties().get("key1"));
        assertNull(problemDetails.additionalProperties().get("key2"));
        assertEquals("value3", problemDetails.additionalProperties().get("key3"));
    }

    @Test
    @DisplayName("Should create ProblemDetails with empty message")
    void shouldCreateProblemDetailsWithEmptyMessage() {
        var problemType = ProblemType.IS_BAD_REQUEST_ERROR;
        var message = "";
        var instance = "/api/test";

        var problemDetails = ProblemDetails.buildBodyResponse(
                problemType,
                message,
                instance,
                null,
                null
        );

        assertNotNull(problemDetails);
        assertEquals("", problemDetails.detail());
    }

    @Test
    @DisplayName("Should create ProblemDetails with long detail message")
    void shouldCreateProblemDetailsWithLongDetailMessage() {
        var problemType = ProblemType.IS_INTERNAL_SERVER_ERROR;
        var longMessage = "A".repeat(1000);
        var instance = "/api/test";

        var problemDetails = ProblemDetails.buildBodyResponse(
                problemType,
                longMessage,
                instance,
                null,
                null
        );

        assertNotNull(problemDetails);
        assertEquals(1000, problemDetails.detail().length());
        assertEquals(longMessage, problemDetails.detail());
    }

}