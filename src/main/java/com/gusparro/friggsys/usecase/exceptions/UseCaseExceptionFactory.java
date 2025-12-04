package com.gusparro.friggsys.usecase.exceptions;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public final class UseCaseExceptionFactory {

    private UseCaseExceptionFactory() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static DuplicateEmailError duplicateEmailError(String email) {
        Map<String, Object> details = Map.of(
                "timestamp", Instant.now(),
                "conflictType", "Duplicate e-mail"
        );

        return new DuplicateEmailError(
                String.format("A user with email '%s' address already exists.", email),
                details
        );
    }

    public static EntityNotFoundError entityNotFoundError(String entityName,
                                                          String identifierType,
                                                          String identifier,
                                                          String operation) {
        Map<String, Object> details = new HashMap<>();
        details.put("searchedAt", Instant.now());
        details.put("resourceType", entityName);
        details.put("identifierType", identifierType);
        details.put("identifier", identifier);
        details.put("operation", operation);

        return new EntityNotFoundError(entityName, identifierType, identifier, details);
    }

    public static MatchingError matchingError(String entityName,
                                              String fieldName,
                                              String operation) {
        Map<String, Object> details = new HashMap<>();
        details.put("searchedAt", Instant.now());
        details.put("resourceType", entityName);
        details.put("fieldName", fieldName);
        details.put("operation", operation);

        return new MatchingError(entityName, fieldName, details);
    }

}
