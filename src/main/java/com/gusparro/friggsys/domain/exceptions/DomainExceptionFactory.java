package com.gusparro.friggsys.domain.exceptions;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class DomainExceptionFactory {

    private DomainExceptionFactory() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static ValidationError emptyField(String fieldName) {
        Map<String, Object> details = Map.of(
                "validationType", "empty_check",
                "timestamp", Instant.now()
        );

        return new ValidationError(
                String.format("%s cannot be empty", fieldName),
                fieldName,
                details
        );
    }

    public static ValidationError minLength(String fieldName, int minLength, int actualLength) {
        Map<String, Object> details = Map.of(
                "validationType", "min_length",
                "minLength", minLength,
                "actualLength", actualLength,
                "missingCharacters", minLength - actualLength,
                "timestamp", Instant.now()
        );

        return new ValidationError(
                String.format("%s must have at least %d characters", fieldName, minLength),
                fieldName,
                details
        );
    }

    public static ValidationError maxLength(String fieldName, int maxLength, int actualLength) {
        Map<String, Object> details = Map.of(
                "validationType", "max_length",
                "maxLength", maxLength,
                "actualLength", actualLength,
                "excessCharacters", actualLength - maxLength,
                "timestamp", Instant.now()
        );

        return new ValidationError(
                String.format("%s cannot exceed %d characters", fieldName, maxLength),
                fieldName,
                details
        );
    }

    public static ValidationError invalidPattern(String fieldName, String pattern, String requirement) {
        Map<String, Object> details = Map.of(
                "validationType", "pattern_mismatch",
                "pattern", pattern,
                "requirement", requirement,
                "timestamp", Instant.now()
        );

        return new ValidationError(
                String.format("%s does not match required pattern", fieldName),
                fieldName,
                details
        );
    }

    public static ValidationError invalid(String fieldName, String message) {
        Map<String, Object> details = Map.of(
                "validationType", "generic",
                "timestamp", Instant.now()
        );

        return new ValidationError(message, fieldName, details);
    }

    public static InvalidStateError invalidState(String entityName, String currentState, String action) {
        Map<String, Object> details = Map.of(
                "timestamp", Instant.now()
        );

        return new InvalidStateError(entityName, currentState, action, details);
    }

    public static InvalidStateError invalidState(String entityName, UUID entityId, String currentState, String action) {
        Map<String, Object> details = new HashMap<>();
        details.put("timestamp", Instant.now());
        details.put("entityId", entityId.toString());

        return new InvalidStateError(entityName, currentState, action, details);
    }

}
