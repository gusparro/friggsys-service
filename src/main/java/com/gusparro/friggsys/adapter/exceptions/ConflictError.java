package com.gusparro.friggsys.adapter.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class ConflictError extends AdapterException {

    private final String entityName;
    private final String action;
    private final String conflictType;

    public ConflictError(String entityName, String action, String conflictType, Map<String, Object> details) {
        var message = String.format("It is not possible to execute '%s' on %s due a '%s' conflict",
                action, entityName, conflictType);

        super(message, details);

        this.entityName = entityName;
        this.action = action;
        this.conflictType = conflictType;
    }

}
