package com.gusparro.friggsys.adapter.exceptions.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProblemType Tests")
class ProblemTypeTest {

    @Test
    @DisplayName("Should have correct status for BAD_REQUEST")
    void shouldHaveCorrectStatusForBadRequest() {
        var problemType = ProblemType.IS_BAD_REQUEST_ERROR;

        assertEquals(400, problemType.getStatus());
        assertEquals("Bad Request", problemType.getTitle());
    }

    @Test
    @DisplayName("Should have correct status for NOT_FOUND")
    void shouldHaveCorrectStatusForNotFound() {
        var problemType = ProblemType.IS_NOT_FOUND_ERROR;

        assertEquals(404, problemType.getStatus());
        assertEquals("Not Found", problemType.getTitle());
    }

    @Test
    @DisplayName("Should have correct status for CONFLICT")
    void shouldHaveCorrectStatusForConflict() {
        var problemType = ProblemType.IS_CONFLICT_ERROR;

        assertEquals(409, problemType.getStatus());
        assertEquals("Conflict", problemType.getTitle());
    }

    @Test
    @DisplayName("Should have correct status for INTERNAL_SERVER_ERROR")
    void shouldHaveCorrectStatusForInternalServerError() {
        var problemType = ProblemType.IS_INTERNAL_SERVER_ERROR;

        assertEquals(500, problemType.getStatus());
        assertEquals("Internal Server Error", problemType.getTitle());
    }

    @Test
    @DisplayName("Should return BAD_REQUEST from status code 400")
    void shouldReturnBadRequestFromStatusCode400() {
        var statusCode = HttpStatus.BAD_REQUEST;

        var problemType = ProblemType.fromStatusCode(statusCode);

        assertNotNull(problemType);
        assertEquals(ProblemType.IS_BAD_REQUEST_ERROR, problemType);
        assertEquals(400, problemType.getStatus());
    }

    @Test
    @DisplayName("Should return NOT_FOUND from status code 404")
    void shouldReturnNotFoundFromStatusCode404() {
        var statusCode = HttpStatus.NOT_FOUND;

        var problemType = ProblemType.fromStatusCode(statusCode);

        assertNotNull(problemType);
        assertEquals(ProblemType.IS_NOT_FOUND_ERROR, problemType);
        assertEquals(404, problemType.getStatus());
    }

    @Test
    @DisplayName("Should return CONFLICT from status code 409")
    void shouldReturnConflictFromStatusCode409() {
        var statusCode = HttpStatus.CONFLICT;

        var problemType = ProblemType.fromStatusCode(statusCode);

        assertNotNull(problemType);
        assertEquals(ProblemType.IS_CONFLICT_ERROR, problemType);
        assertEquals(409, problemType.getStatus());
    }

    @Test
    @DisplayName("Should return INTERNAL_SERVER_ERROR from status code 500")
    void shouldReturnInternalServerErrorFromStatusCode500() {
        var statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

        var problemType = ProblemType.fromStatusCode(statusCode);

        assertNotNull(problemType);
        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR, problemType);
        assertEquals(500, problemType.getStatus());
    }

    @Test
    @DisplayName("Should return INTERNAL_SERVER_ERROR for unmapped status code")
    void shouldReturnInternalServerErrorForUnmappedStatusCode() {
        var statusCode = HttpStatus.UNAUTHORIZED;

        var problemType = ProblemType.fromStatusCode(statusCode);

        assertNotNull(problemType);
        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR, problemType);
    }

    @Test
    @DisplayName("Should return INTERNAL_SERVER_ERROR for OK status code")
    void shouldReturnInternalServerErrorForOkStatusCode() {
        var statusCode = HttpStatus.OK;

        var problemType = ProblemType.fromStatusCode(statusCode);

        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR, problemType);
    }

    @Test
    @DisplayName("Should return INTERNAL_SERVER_ERROR for CREATED status code")
    void shouldReturnInternalServerErrorForCreatedStatusCode() {
        var statusCode = HttpStatus.CREATED;

        var problemType = ProblemType.fromStatusCode(statusCode);

        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR, problemType);
    }

    @Test
    @DisplayName("Should return INTERNAL_SERVER_ERROR for FORBIDDEN status code")
    void shouldReturnInternalServerErrorForForbiddenStatusCode() {
        var statusCode = HttpStatus.FORBIDDEN;

        var problemType = ProblemType.fromStatusCode(statusCode);

        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR, problemType);
    }

    @Test
    @DisplayName("Should return INTERNAL_SERVER_ERROR for METHOD_NOT_ALLOWED status code")
    void shouldReturnInternalServerErrorForMethodNotAllowedStatusCode() {
        var statusCode = HttpStatus.METHOD_NOT_ALLOWED;

        var problemType = ProblemType.fromStatusCode(statusCode);

        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR, problemType);
    }

    @Test
    @DisplayName("Should return INTERNAL_SERVER_ERROR for UNPROCESSABLE_ENTITY status code")
    void shouldReturnInternalServerErrorForUnprocessableEntityStatusCode() {
        var statusCode = HttpStatus.UNPROCESSABLE_ENTITY;

        var problemType = ProblemType.fromStatusCode(statusCode);

        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR, problemType);
    }

    @Test
    @DisplayName("Should return INTERNAL_SERVER_ERROR for BAD_GATEWAY status code")
    void shouldReturnInternalServerErrorForBadGatewayStatusCode() {
        var statusCode = HttpStatus.BAD_GATEWAY;

        var problemType = ProblemType.fromStatusCode(statusCode);

        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR, problemType);
    }

    @Test
    @DisplayName("Should return INTERNAL_SERVER_ERROR for SERVICE_UNAVAILABLE status code")
    void shouldReturnInternalServerErrorForServiceUnavailableStatusCode() {
        var statusCode = HttpStatus.SERVICE_UNAVAILABLE;

        var problemType = ProblemType.fromStatusCode(statusCode);

        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR, problemType);
    }

    @Test
    @DisplayName("Should return INTERNAL_SERVER_ERROR for non-HttpStatus implementation")
    void shouldReturnInternalServerErrorForNonHttpStatusImplementation() {
        HttpStatusCode statusCode = HttpStatusCode.valueOf(418);

        var problemType = ProblemType.fromStatusCode(statusCode);

        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR, problemType);
    }

    @Test
    @DisplayName("Should return INTERNAL_SERVER_ERROR for custom status code")
    void shouldReturnInternalServerErrorForCustomStatusCode() {
        HttpStatusCode statusCode = HttpStatusCode.valueOf(999);

        var problemType = ProblemType.fromStatusCode(statusCode);

        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR, problemType);
    }

    @Test
    @DisplayName("Should have exactly 4 enum constants")
    void shouldHaveExactly4EnumConstants() {
        var values = ProblemType.values();

        assertEquals(4, values.length);
    }

    @Test
    @DisplayName("Should return correct enum by name")
    void shouldReturnCorrectEnumByName() {
        var badRequest = ProblemType.valueOf("IS_BAD_REQUEST_ERROR");
        var notFound = ProblemType.valueOf("IS_NOT_FOUND_ERROR");
        var conflict = ProblemType.valueOf("IS_CONFLICT_ERROR");
        var serverError = ProblemType.valueOf("IS_INTERNAL_SERVER_ERROR");

        assertEquals(ProblemType.IS_BAD_REQUEST_ERROR, badRequest);
        assertEquals(ProblemType.IS_NOT_FOUND_ERROR, notFound);
        assertEquals(ProblemType.IS_CONFLICT_ERROR, conflict);
        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR, serverError);
    }

    @Test
    @DisplayName("Should throw exception for invalid enum name")
    void shouldThrowExceptionForInvalidEnumName() {
        assertThrows(IllegalArgumentException.class, () -> {
            ProblemType.valueOf("INVALID_TYPE");
        });
    }

    @Test
    @DisplayName("Should have unique status codes for each enum constant")
    void shouldHaveUniqueStatusCodesForEachEnumConstant() {
        var badRequest = ProblemType.IS_BAD_REQUEST_ERROR.getStatus();
        var notFound = ProblemType.IS_NOT_FOUND_ERROR.getStatus();
        var conflict = ProblemType.IS_CONFLICT_ERROR.getStatus();
        var serverError = ProblemType.IS_INTERNAL_SERVER_ERROR.getStatus();

        assertNotEquals(badRequest, notFound);
        assertNotEquals(badRequest, conflict);
        assertNotEquals(badRequest, serverError);
        assertNotEquals(notFound, conflict);
        assertNotEquals(notFound, serverError);
        assertNotEquals(conflict, serverError);
    }

    @Test
    @DisplayName("Should have unique titles for each enum constant")
    void shouldHaveUniqueTitlesForEachEnumConstant() {
        var badRequest = ProblemType.IS_BAD_REQUEST_ERROR.getTitle();
        var notFound = ProblemType.IS_NOT_FOUND_ERROR.getTitle();
        var conflict = ProblemType.IS_CONFLICT_ERROR.getTitle();
        var serverError = ProblemType.IS_INTERNAL_SERVER_ERROR.getTitle();

        assertNotEquals(badRequest, notFound);
        assertNotEquals(badRequest, conflict);
        assertNotEquals(badRequest, serverError);
        assertNotEquals(notFound, conflict);
        assertNotEquals(notFound, serverError);
        assertNotEquals(conflict, serverError);
    }

    @Test
    @DisplayName("Should maintain consistency between fromStatusCode and direct enum access")
    void shouldMaintainConsistencyBetweenFromStatusCodeAndDirectEnumAccess() {
        var badRequestDirect = ProblemType.IS_BAD_REQUEST_ERROR;
        var badRequestFromCode = ProblemType.fromStatusCode(HttpStatus.BAD_REQUEST);

        assertEquals(badRequestDirect, badRequestFromCode);
        assertEquals(badRequestDirect.getStatus(), badRequestFromCode.getStatus());
        assertEquals(badRequestDirect.getTitle(), badRequestFromCode.getTitle());
    }

    @Test
    @DisplayName("Should handle all defined HttpStatus correctly")
    void shouldHandleAllDefinedHttpStatusCorrectly() {
        var badRequest = ProblemType.fromStatusCode(HttpStatus.BAD_REQUEST);
        var notFound = ProblemType.fromStatusCode(HttpStatus.NOT_FOUND);
        var conflict = ProblemType.fromStatusCode(HttpStatus.CONFLICT);
        var serverError = ProblemType.fromStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        assertEquals(400, badRequest.getStatus());
        assertEquals(404, notFound.getStatus());
        assertEquals(409, conflict.getStatus());
        assertEquals(500, serverError.getStatus());
    }

    @Test
    @DisplayName("Should return non-null title for all enum constants")
    void shouldReturnNonNullTitleForAllEnumConstants() {
        for (ProblemType problemType : ProblemType.values()) {
            assertNotNull(problemType.getTitle());
            assertFalse(problemType.getTitle().isEmpty());
        }
    }

    @Test
    @DisplayName("Should return non-null status for all enum constants")
    void shouldReturnNonNullStatusForAllEnumConstants() {
        for (ProblemType problemType : ProblemType.values()) {
            assertNotNull(problemType.getStatus());
            assertTrue(problemType.getStatus() > 0);
        }
    }

    @Test
    @DisplayName("Should match HttpStatus reason phrase for all enum constants")
    void shouldMatchHttpStatusReasonPhraseForAllEnumConstants() {
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ProblemType.IS_BAD_REQUEST_ERROR.getTitle());
        assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(),
                ProblemType.IS_NOT_FOUND_ERROR.getTitle());
        assertEquals(HttpStatus.CONFLICT.getReasonPhrase(),
                ProblemType.IS_CONFLICT_ERROR.getTitle());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ProblemType.IS_INTERNAL_SERVER_ERROR.getTitle());
    }

    @Test
    @DisplayName("Should match HttpStatus value for all enum constants")
    void shouldMatchHttpStatusValueForAllEnumConstants() {
        assertEquals(HttpStatus.BAD_REQUEST.value(),
                ProblemType.IS_BAD_REQUEST_ERROR.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.value(),
                ProblemType.IS_NOT_FOUND_ERROR.getStatus());
        assertEquals(HttpStatus.CONFLICT.value(),
                ProblemType.IS_CONFLICT_ERROR.getStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ProblemType.IS_INTERNAL_SERVER_ERROR.getStatus());
    }

    @Test
    @DisplayName("Should be consistent when calling fromStatusCode multiple times")
    void shouldBeConsistentWhenCallingFromStatusCodeMultipleTimes() {
        var first = ProblemType.fromStatusCode(HttpStatus.BAD_REQUEST);
        var second = ProblemType.fromStatusCode(HttpStatus.BAD_REQUEST);
        var third = ProblemType.fromStatusCode(HttpStatus.BAD_REQUEST);

        assertEquals(first, second);
        assertEquals(second, third);
        assertEquals(first, third);
    }

    @Test
    @DisplayName("Should return INTERNAL_SERVER_ERROR for all 2xx status codes")
    void shouldReturnInternalServerErrorForAll2xxStatusCodes() {
        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR,
                ProblemType.fromStatusCode(HttpStatus.OK));
        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR,
                ProblemType.fromStatusCode(HttpStatus.CREATED));
        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR,
                ProblemType.fromStatusCode(HttpStatus.ACCEPTED));
        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR,
                ProblemType.fromStatusCode(HttpStatus.NO_CONTENT));
    }

    @Test
    @DisplayName("Should return INTERNAL_SERVER_ERROR for all 3xx status codes")
    void shouldReturnInternalServerErrorForAll3xxStatusCodes() {
        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR,
                ProblemType.fromStatusCode(HttpStatus.MOVED_PERMANENTLY));
        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR,
                ProblemType.fromStatusCode(HttpStatus.FOUND));
        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR,
                ProblemType.fromStatusCode(HttpStatus.TEMPORARY_REDIRECT));
        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR,
                ProblemType.fromStatusCode(HttpStatus.PERMANENT_REDIRECT));
    }

    @Test
    @DisplayName("Should return INTERNAL_SERVER_ERROR for unmapped 4xx status codes")
    void shouldReturnInternalServerErrorForUnmapped4xxStatusCodes() {
        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR,
                ProblemType.fromStatusCode(HttpStatus.UNAUTHORIZED));
        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR,
                ProblemType.fromStatusCode(HttpStatus.FORBIDDEN));
        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR,
                ProblemType.fromStatusCode(HttpStatus.METHOD_NOT_ALLOWED));
    }

    @Test
    @DisplayName("Should return INTERNAL_SERVER_ERROR for unmapped 5xx status codes")
    void shouldReturnInternalServerErrorForUnmapped5xxStatusCodes() {
        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR,
                ProblemType.fromStatusCode(HttpStatus.NOT_IMPLEMENTED));
        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR,
                ProblemType.fromStatusCode(HttpStatus.BAD_GATEWAY));
        assertEquals(ProblemType.IS_INTERNAL_SERVER_ERROR,
                ProblemType.fromStatusCode(HttpStatus.SERVICE_UNAVAILABLE));
    }

    @Test
    @DisplayName("Should have status codes in 4xx or 5xx range")
    void shouldHaveStatusCodesIn4xxOr5xxRange() {
        for (ProblemType problemType : ProblemType.values()) {
            var status = problemType.getStatus();
            assertTrue(status >= 400 && status < 600,
                    "Status " + status + " should be in 4xx or 5xx range");
        }
    }

    @Test
    @DisplayName("Should have immutable status and title")
    void shouldHaveImmutableStatusAndTitle() {
        var problemType = ProblemType.IS_BAD_REQUEST_ERROR;
        var statusBefore = problemType.getStatus();
        var titleBefore = problemType.getTitle();
        var statusAfter = problemType.getStatus();
        var titleAfter = problemType.getTitle();

        assertEquals(statusBefore, statusAfter);
        assertEquals(titleBefore, titleAfter);
    }

}