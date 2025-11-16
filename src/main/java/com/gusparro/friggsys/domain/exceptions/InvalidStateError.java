package com.gusparro.friggsys.domain.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class InvalidStateError extends DomainException {

    private final String entityName;
    private final String currentState;
    private final String action;

    public InvalidStateError(String entityName, String currentState, String action) {
        this(entityName, currentState, action, null);
    }

    public InvalidStateError(String entityName, String currentState, String action, Map<String, Object> details) {
        var message = String.format("It is not possible to execute '%s' on %s in the '%s' state",
                action, entityName, currentState);

        super(message, details);

        this.entityName = entityName;
        this.currentState = currentState;
        this.action = action;
    }
}
