package com.gusparro.friggsys.adapter.exceptions;

import com.gusparro.friggsys.domain.exceptions.InvalidStateError;
import com.gusparro.friggsys.domain.exceptions.ValidationError;
import com.gusparro.friggsys.usecase.exceptions.EntityNotFoundError;

import java.time.Instant;
import java.util.Map;

public final class AdapterExceptionFactory {

    private AdapterExceptionFactory() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static BadResquestError statusConflict(InvalidStateError error) {
        return new BadResquestError(error.getMessage(), error.getEntityName(),
                error.getAction(), "status", error.getDetails());
    }

    public static BadResquestError invalidField(String entityName, String action, ValidationError error) {
        var message = String.format("It is not possible to execute '%s' on %s due a problem with the '%s' field, %s",
                action, entityName, error.getField(), error.getMessage());

        return new BadResquestError(message, entityName, action, error.getField(), error.getDetails());
    }

    public static BadResquestError entityNotFound(String action, EntityNotFoundError error) {
        return new BadResquestError(error.getEntityName(), action, error.getIdentifierType(), error.getDetails());
    }

    public static NotFoundError resourceNotExists(EntityNotFoundError error) {
        return new NotFoundError(error.getEntityName(), error.getIdentifierType(), error.getDetails());
    }

    public static ConflictError conflictError(String entityName, String action, String conflictType) {
        Map<String, Object> details = Map.of(
                "conflictType", conflictType,
                "entity", entityName,
                "action", action,
                "timestamp", Instant.now()
        );

        return new ConflictError(entityName, action, conflictType, details);
    }

}
