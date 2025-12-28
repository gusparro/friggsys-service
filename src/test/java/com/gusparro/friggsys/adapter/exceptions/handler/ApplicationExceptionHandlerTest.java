package com.gusparro.friggsys.adapter.exceptions.handler;

import com.gusparro.friggsys.adapter.exceptions.BadResquestError;
import com.gusparro.friggsys.adapter.exceptions.ConflictError;
import com.gusparro.friggsys.adapter.exceptions.NotFoundError;
import com.gusparro.friggsys.usecase.exceptions.DuplicateEmailError;
import com.gusparro.friggsys.usecase.exceptions.MatchingError;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApplicationExceptionHandler Tests")
class ApplicationExceptionHandlerTest {

    @InjectMocks
    private ApplicationExceptionHandler handler;

    @Mock
    private WebRequest webRequest;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpMessageNotReadableException httpMessageNotReadableException;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @Test
    @DisplayName("Should handle BadRequestError correctly")
    void shouldHandleBadRequestErrorCorrectly() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        Map<String, Object> details = Map.of("field", "username", "value", "test");
        var error = new BadResquestError("Invalid request", "User", "create", details);

        var response = handler.handleBadRequestError(error, webRequest, httpServletRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ProblemDetails.class, response.getBody());

        var problemDetails = (ProblemDetails) response.getBody();
        assertNotNull(problemDetails.detail());
        assertTrue(problemDetails.detail().contains("User"));
        assertTrue(problemDetails.detail().contains("create"));
        assertEquals(400, problemDetails.status());
        assertEquals("/api/test", problemDetails.instance());
    }

    @Test
    @DisplayName("Should handle ConflictError correctly")
    void shouldHandleConflictErrorCorrectly() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        Map<String, Object> details = Map.of("conflictType", "duplicate_email", "userId", "123");
        var error = new ConflictError("Resource already exists", "User", "create", details);

        var response = handler.handleConflictError(error, webRequest, httpServletRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ProblemDetails.class, response.getBody());

        var problemDetails = (ProblemDetails) response.getBody();
        assertNotNull(problemDetails.detail());
        assertTrue(problemDetails.detail().contains("User"));
        assertTrue(problemDetails.detail().contains("create"));
        assertEquals(409, problemDetails.status());
        assertEquals("/api/test", problemDetails.instance());
    }

    @Test
    @DisplayName("Should handle NotFoundError correctly")
    void shouldHandleNotFoundErrorCorrectly() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        Map<String, Object> details = Map.of("entityId", "999", "searchedField", "ID");
        var error = new NotFoundError("Resource not found", "User", details);

        var response = handler.handleNotFoundError(error, webRequest, httpServletRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ProblemDetails.class, response.getBody());

        var problemDetails = (ProblemDetails) response.getBody();
        assertNotNull(problemDetails.detail());
        assertTrue(problemDetails.detail().contains("User"));
        assertEquals(404, problemDetails.status());
        assertEquals("/api/test", problemDetails.instance());
    }

    @Test
    @DisplayName("Should handle MatchingError correctly")
    void shouldHandleMatchingErrorCorrectly() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        Map<String, Object> details = Map.of("field", "password", "reason", "does not match");
        var error = new MatchingError("Password does not match", "password", details);

        var response = handler.handleMatchingError(error, webRequest, httpServletRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ProblemDetails.class, response.getBody());

        var problemDetails = (ProblemDetails) response.getBody();
        assertNotNull(problemDetails.detail());
        assertTrue(problemDetails.detail().contains("password"));
        assertEquals(400, problemDetails.status());
        assertEquals("/api/test", problemDetails.instance());
    }

    @Test
    @DisplayName("Should handle DuplicateEmailError correctly")
    void shouldHandleDuplicateEmailErrorCorrectly() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        Map<String, Object> details = Map.of("conflictField", "email", "attemptedValue", "test@example.com");
        var error = new DuplicateEmailError("Email already exists", details);

        var response = handler.handleDuplicateEmailError(error, webRequest, httpServletRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ProblemDetails.class, response.getBody());

        var problemDetails = (ProblemDetails) response.getBody();
        assertNotNull(problemDetails.detail());
        assertEquals(400, problemDetails.status());
        assertEquals("/api/test", problemDetails.instance());
    }

    @Test
    @DisplayName("Should handle generic Exception correctly")
    void shouldHandleGenericExceptionCorrectly() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        var exception = new RuntimeException("Unexpected error occurred");

        var response = handler.handleGenericException(exception, webRequest, httpServletRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ProblemDetails.class, response.getBody());

        var problemDetails = (ProblemDetails) response.getBody();
        assertEquals("Unexpected error occurred", problemDetails.detail());
        assertEquals(500, problemDetails.status());
        assertEquals("/api/test", problemDetails.instance());
    }

    @Test
    @DisplayName("Should handle HttpMessageNotReadableException correctly")
    void shouldHandleHttpMessageNotReadableExceptionCorrectly() {
        var response = handler.handleHttpMessageNotReadable(
                httpMessageNotReadableException,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                webRequest
        );

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ProblemDetails.class, response.getBody());

        var problemDetails = (ProblemDetails) response.getBody();
        assertEquals("Request body is malformed or has an invalid type.", problemDetails.detail());
        assertEquals(400, problemDetails.status());
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException correctly")
    void shouldHandleMethodArgumentNotValidExceptionCorrectly() {
        var fieldError1 = new FieldError("user", "username", "Username is required");
        var fieldError2 = new FieldError("user", "email", "Invalid email format");

        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        var response = handler.handleMethodArgumentNotValid(
                methodArgumentNotValidException,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                webRequest
        );

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ProblemDetails.class, response.getBody());

        var problemDetails = (ProblemDetails) response.getBody();
        assertEquals("One or more fields validation failed.", problemDetails.detail());
        assertEquals(400, problemDetails.status());
        assertNotNull(problemDetails.fields());
        assertEquals(2, problemDetails.fields().size());
        assertEquals("username", problemDetails.fields().get(0).name());
        assertEquals("Username is required", problemDetails.fields().get(0).message());
        assertEquals("email", problemDetails.fields().get(1).name());
        assertEquals("Invalid email format", problemDetails.fields().get(1).message());
    }

    @Test
    @DisplayName("Should sanitize forbidden keys from details")
    void shouldSanitizeForbiddenKeysFromDetails() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        var details = new HashMap<String, Object>();
        details.put("ID", "123");
        details.put("password", "secret123");
        details.put("token", "abc-token");
        details.put("email", "user@example.com");
        details.put("username", "testuser");
        details.put("age", 25);

        var error = new BadResquestError("Invalid request", "User", "create", details);

        var response = handler.handleBadRequestError(error, webRequest, httpServletRequest);

        assertNotNull(response);
        var problemDetails = (ProblemDetails) response.getBody();
        assertNotNull(problemDetails);
        assertNotNull(problemDetails.additionalProperties());

        assertFalse(problemDetails.additionalProperties().containsKey("ID"));
        assertFalse(problemDetails.additionalProperties().containsKey("password"));
        assertFalse(problemDetails.additionalProperties().containsKey("token"));
        assertFalse(problemDetails.additionalProperties().containsKey("email"));
        assertTrue(problemDetails.additionalProperties().containsKey("username"));
        assertTrue(problemDetails.additionalProperties().containsKey("age"));
        assertEquals("testuser", problemDetails.additionalProperties().get("username"));
        assertEquals(25, problemDetails.additionalProperties().get("age"));
    }

    @Test
    @DisplayName("Should handle empty details map")
    void shouldHandleEmptyDetailsMap() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        var error = new BadResquestError("Invalid request", "User", "create", new HashMap<>());

        var response = handler.handleBadRequestError(error, webRequest, httpServletRequest);

        assertNotNull(response);
        var problemDetails = (ProblemDetails) response.getBody();
        assertNotNull(problemDetails);
        assertNotNull(problemDetails.additionalProperties());
        assertTrue(problemDetails.additionalProperties().isEmpty());
    }

    @Test
    @DisplayName("Should handle details with only forbidden keys")
    void shouldHandleDetailsWithOnlyForbiddenKeys() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        var details = new HashMap<String, Object>();
        details.put("ID", "123");
        details.put("password", "secret");
        details.put("token", "token123");
        details.put("email", "test@test.com");

        var error = new ConflictError("Conflict", "User", "create", details);

        var response = handler.handleConflictError(error, webRequest, httpServletRequest);

        assertNotNull(response);
        var problemDetails = (ProblemDetails) response.getBody();
        assertNotNull(problemDetails);
        assertNotNull(problemDetails.additionalProperties());
        assertTrue(problemDetails.additionalProperties().isEmpty());
    }

    @Test
    @DisplayName("Should preserve non-forbidden keys after sanitization")
    void shouldPreserveNonForbiddenKeysAfterSanitization() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        var details = new HashMap<String, Object>();
        details.put("userId", "456");
        details.put("operation", "update");
        details.put("timestamp", "2025-01-01T10:00:00");
        details.put("password", "should-be-removed");

        var error = new NotFoundError("Not found", "User", details);

        var response = handler.handleNotFoundError(error, webRequest, httpServletRequest);

        assertNotNull(response);
        var problemDetails = (ProblemDetails) response.getBody();
        assertNotNull(problemDetails.additionalProperties());
        assertEquals(3, problemDetails.additionalProperties().size());
        assertTrue(problemDetails.additionalProperties().containsKey("userId"));
        assertTrue(problemDetails.additionalProperties().containsKey("operation"));
        assertTrue(problemDetails.additionalProperties().containsKey("timestamp"));
        assertFalse(problemDetails.additionalProperties().containsKey("password"));
    }

    @Test
    @DisplayName("Should handle null exception message gracefully")
    void shouldHandleNullExceptionMessageGracefully() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        var exception = new RuntimeException((String) null);

        var response = handler.handleGenericException(exception, webRequest, httpServletRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        var problemDetails = (ProblemDetails) response.getBody();
        assertNotNull(problemDetails);
    }

    @Test
    @DisplayName("Should use correct URI from HttpServletRequest")
    void shouldUseCorrectUriFromHttpServletRequest() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/users/123/update");
        var error = new BadResquestError("Error", "User", "update", Map.of());

        var response = handler.handleBadRequestError(error, webRequest, httpServletRequest);

        var problemDetails = (ProblemDetails) response.getBody();
        assertNotNull(problemDetails);
        assertEquals("/api/users/123/update", problemDetails.instance());
    }

    @Test
    @DisplayName("Should handle different URI paths correctly")
    void shouldHandleDifferentUriPathsCorrectly() {
        when(httpServletRequest.getRequestURI()).thenReturn("/v1/products/search");
        var error = new NotFoundError("Product not found", "Product", Map.of());

        var response = handler.handleNotFoundError(error, webRequest, httpServletRequest);

        var problemDetails = (ProblemDetails) response.getBody();
        assertEquals("/v1/products/search", problemDetails.instance());
    }

    @Test
    @DisplayName("Should return correct HTTP status for each exception type")
    void shouldReturnCorrectHttpStatusForEachExceptionType() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        var badRequestError = new BadResquestError("Bad request", "Entity", "action", Map.of());
        var conflictError = new ConflictError("Conflict", "Entity", "action", Map.of());
        var notFoundError = new NotFoundError("Not found", "Entity", Map.of());
        var matchingError = new MatchingError("Matching failed", "password", Map.of());
        var duplicateEmailError = new DuplicateEmailError("Duplicate email", Map.of());
        var genericException = new RuntimeException("Error");

        assertEquals(HttpStatus.BAD_REQUEST,
                handler.handleBadRequestError(badRequestError, webRequest, httpServletRequest).getStatusCode());
        assertEquals(HttpStatus.CONFLICT,
                handler.handleConflictError(conflictError, webRequest, httpServletRequest).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND,
                handler.handleNotFoundError(notFoundError, webRequest, httpServletRequest).getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST,
                handler.handleMatchingError(matchingError, webRequest, httpServletRequest).getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST,
                handler.handleDuplicateEmailError(duplicateEmailError, webRequest, httpServletRequest).getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
                handler.handleGenericException(genericException, webRequest, httpServletRequest).getStatusCode());
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with no field errors")
    void shouldHandleMethodArgumentNotValidExceptionWithNoFieldErrors() {
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());

        var response = handler.handleMethodArgumentNotValid(
                methodArgumentNotValidException,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                webRequest
        );

        assertNotNull(response);
        var problemDetails = (ProblemDetails) response.getBody();
        assertNotNull(problemDetails);
        assertNotNull(problemDetails.fields());
        assertTrue(problemDetails.fields().isEmpty());
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with single field error")
    void shouldHandleMethodArgumentNotValidExceptionWithSingleFieldError() {
        var fieldError = new FieldError("user", "age", "Age must be positive");

        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        var response = handler.handleMethodArgumentNotValid(
                methodArgumentNotValidException,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                webRequest
        );

        var problemDetails = (ProblemDetails) response.getBody();
        assertNotNull(problemDetails);
        assertEquals(1, problemDetails.fields().size());
        assertEquals("age", problemDetails.fields().get(0).name());
        assertEquals("Age must be positive", problemDetails.fields().get(0).message());
    }

    @Test
    @DisplayName("Should sanitize case-sensitive forbidden keys")
    void shouldSanitizeCaseSensitiveForbiddenKeys() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        var details = new HashMap<String, Object>();
        details.put("ID", "should-be-removed");
        details.put("id", "should-remain");
        details.put("Password", "should-remain");
        details.put("password", "should-be-removed");
        details.put("Email", "should-remain");
        details.put("email", "should-be-removed");

        var error = new BadResquestError("Error", "Entity", "action", details);

        var response = handler.handleBadRequestError(error, webRequest, httpServletRequest);

        var problemDetails = (ProblemDetails) response.getBody();
        assertNotNull(problemDetails.additionalProperties());
        assertFalse(problemDetails.additionalProperties().containsKey("ID"));
        assertTrue(problemDetails.additionalProperties().containsKey("id"));
        assertFalse(problemDetails.additionalProperties().containsKey("password"));
        assertTrue(problemDetails.additionalProperties().containsKey("Password"));
        assertFalse(problemDetails.additionalProperties().containsKey("email"));
        assertTrue(problemDetails.additionalProperties().containsKey("Email"));
    }

    @Test
    @DisplayName("Should handle complex nested details after sanitization")
    void shouldHandleComplexNestedDetailsAfterSanitization() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        var details = new HashMap<String, Object>();
        details.put("user", Map.of("name", "John", "age", 30));
        details.put("metadata", Map.of("timestamp", "2025-01-01", "source", "api"));
        details.put("password", "secret");

        var error = new ConflictError("Conflict", "User", "create", details);

        var response = handler.handleConflictError(error, webRequest, httpServletRequest);

        var problemDetails = (ProblemDetails) response.getBody();
        assertNotNull(problemDetails.additionalProperties());
        assertTrue(problemDetails.additionalProperties().containsKey("user"));
        assertTrue(problemDetails.additionalProperties().containsKey("metadata"));
        assertFalse(problemDetails.additionalProperties().containsKey("password"));
    }

    @Test
    @DisplayName("Should maintain original details immutability")
    void shouldMaintainOriginalDetailsImmutability() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        Map<String, Object> originalDetails = Map.of("password", "secret", "username", "user");
        var error = new BadResquestError("Error", "Entity", "action", originalDetails);

        handler.handleBadRequestError(error, webRequest, httpServletRequest);

        // Original map should remain unchanged
        assertTrue(originalDetails.containsKey("password"));
        assertTrue(originalDetails.containsKey("username"));
        assertEquals(2, originalDetails.size());
    }

    @Test
    @DisplayName("Should handle all usecase exceptions with BAD_REQUEST status")
    void shouldHandleAllUsecaseExceptionsWithBadRequestStatus() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        var matchingError = new MatchingError("Matching error", "password", Map.of());
        var duplicateEmailError = new DuplicateEmailError("Duplicate email", Map.of());

        var matchingResponse = handler.handleMatchingError(matchingError, webRequest, httpServletRequest);
        var duplicateEmailResponse = handler.handleDuplicateEmailError(duplicateEmailError, webRequest, httpServletRequest);

        assertEquals(HttpStatus.BAD_REQUEST, matchingResponse.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, duplicateEmailResponse.getStatusCode());
    }

    @Test
    @DisplayName("Should create ProblemDetails with correct ProblemType for each exception")
    void shouldCreateProblemDetailsWithCorrectProblemTypeForEachException() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        var badRequestError = new BadResquestError("Error", "Entity", "action", Map.of());
        var conflictError = new ConflictError("Error", "Entity", "action", Map.of());
        var notFoundError = new NotFoundError("Error", "Entity", Map.of());

        var badRequestResponse = handler.handleBadRequestError(badRequestError, webRequest, httpServletRequest);
        var conflictResponse = handler.handleConflictError(conflictError, webRequest, httpServletRequest);
        var notFoundResponse = handler.handleNotFoundError(notFoundError, webRequest, httpServletRequest);

        var badRequestDetails = (ProblemDetails) badRequestResponse.getBody();
        var conflictDetails = (ProblemDetails) conflictResponse.getBody();
        var notFoundDetails = (ProblemDetails) notFoundResponse.getBody();

        assertEquals(ProblemType.IS_BAD_REQUEST_ERROR.getTitle(), badRequestDetails.title());
        assertEquals(ProblemType.IS_CONFLICT_ERROR.getTitle(), conflictDetails.title());
        assertEquals(ProblemType.IS_NOT_FOUND_ERROR.getTitle(), notFoundDetails.title());
    }

    @Test
    @DisplayName("Should handle multiple consecutive exception calls")
    void shouldHandleMultipleConsecutiveExceptionCalls() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        var error1 = new BadResquestError("Error 1", "User", "create", Map.of());
        var error2 = new BadResquestError("Error 2", "Order", "update", Map.of());
        var error3 = new BadResquestError("Error 3", "Product", "delete", Map.of());

        var response1 = handler.handleBadRequestError(error1, webRequest, httpServletRequest);
        var response2 = handler.handleBadRequestError(error2, webRequest, httpServletRequest);
        var response3 = handler.handleBadRequestError(error3, webRequest, httpServletRequest);

        assertNotNull(response1);
        assertNotNull(response2);
        assertNotNull(response3);

        var details1 = (ProblemDetails) response1.getBody();
        var details2 = (ProblemDetails) response2.getBody();
        var details3 = (ProblemDetails) response3.getBody();

        assertNotNull(details1.detail());
        assertNotNull(details2.detail());
        assertNotNull(details3.detail());
        assertTrue(details1.detail().contains("User"));
        assertTrue(details2.detail().contains("Order"));
        assertTrue(details3.detail().contains("Product"));
    }

    @Test
    @DisplayName("Should preserve error entity names in ProblemDetails")
    void shouldPreserveErrorEntityNamesInProblemDetails() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        var error = new BadResquestError("Custom message", "CustomEntity", "customAction", Map.of());

        var response = handler.handleBadRequestError(error, webRequest, httpServletRequest);

        var problemDetails = (ProblemDetails) response.getBody();
        assertTrue(problemDetails.detail().contains("CustomEntity"));
        assertTrue(problemDetails.detail().contains("customAction"));
    }

    @Test
    @DisplayName("Should handle exceptions with special characters in entity names")
    void shouldHandleExceptionsWithSpecialCharactersInEntityNames() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        var error = new NotFoundError("Not found", "Entity@123", Map.of());

        var response = handler.handleNotFoundError(error, webRequest, httpServletRequest);

        var problemDetails = (ProblemDetails) response.getBody();
        assertNotNull(problemDetails.detail());
        assertTrue(problemDetails.detail().contains("Entity@123"));
    }

}