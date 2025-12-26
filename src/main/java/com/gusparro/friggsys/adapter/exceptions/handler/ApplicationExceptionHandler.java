package com.gusparro.friggsys.adapter.exceptions.handler;

import com.gusparro.friggsys.adapter.exceptions.BadResquestError;
import com.gusparro.friggsys.adapter.exceptions.ConflictError;
import com.gusparro.friggsys.adapter.exceptions.NotFoundError;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.gusparro.friggsys.adapter.exceptions.handler.ProblemType.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception exception,
                                                             Object body,
                                                             @NonNull HttpHeaders headers,
                                                             @NonNull HttpStatusCode statusCode,
                                                             @NonNull WebRequest request) {
        if (body == null) {
            var problemType = ProblemType.fromStatusCode(statusCode);
            body = ProblemDetails.buildBodyResponse(problemType, exception.getMessage(),
                    null, null, null);

            return super.handleExceptionInternal(exception, body, headers, statusCode, request);
        }

        return super.handleExceptionInternal(exception, body, headers, statusCode, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException exception,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {

        var body = ProblemDetails.buildBodyResponse(IS_BAD_REQUEST_ERROR,
                "Request body is malformed or has an invalid type.",
                null, null, null);

        return handleExceptionInternal(exception, body, new HttpHeaders(), BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        var fields = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> FieldValidationDetail.builder()
                        .name(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build()
                ).toList();

        var body = ProblemDetails.buildBodyResponse(IS_BAD_REQUEST_ERROR, "One or more fields validation failed.",
                null, fields, null);

        return handleExceptionInternal(exception, body, new HttpHeaders(), BAD_REQUEST, request);
    }

    @ExceptionHandler(BadResquestError.class)
    public ResponseEntity<?> handleBadRequestError(BadResquestError error, WebRequest request, HttpServletRequest http) {
        var body = ProblemDetails.buildBodyResponse(IS_BAD_REQUEST_ERROR, error.getMessage(),
                http.getRequestURI(), null, sanitizeMapData(error.getDetails()));

        return handleExceptionInternal(error, body, new HttpHeaders(), BAD_REQUEST, request);
    }

    @ExceptionHandler(ConflictError.class)
    public ResponseEntity<?> handleConflictError(ConflictError error, WebRequest request, HttpServletRequest http) {
        var body = ProblemDetails.buildBodyResponse(IS_CONFLICT_ERROR, error.getMessage(),
                http.getRequestURI(), null, sanitizeMapData(error.getDetails()));

        return handleExceptionInternal(error, body, new HttpHeaders(), CONFLICT, request);
    }

    @ExceptionHandler(NotFoundError.class)
    public ResponseEntity<?> handleNotFoundError(NotFoundError error, WebRequest request, HttpServletRequest http) {
        var body = ProblemDetails.buildBodyResponse(IS_NOT_FOUND_ERROR, error.getMessage(),
                http.getRequestURI(), null, sanitizeMapData(error.getDetails()));

        return handleExceptionInternal(error, body, new HttpHeaders(), NOT_FOUND, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception exception, WebRequest request, HttpServletRequest http) {
        var body = ProblemDetails.buildBodyResponse(IS_INTERNAL_SERVER_ERROR, exception.getMessage(),
                http.getRequestURI() , null, null);

        return handleExceptionInternal(exception, body, new HttpHeaders(), INTERNAL_SERVER_ERROR, request);
    }

    private Map<String, Object> sanitizeMapData(Map<String, Object> details) {
        var forbiddenKeys = Arrays.asList("ID", "password", "token", "email");
        var mutableDetails = new HashMap<>(details);

        forbiddenKeys.forEach(mutableDetails::remove);

        return  mutableDetails;
    }

}
