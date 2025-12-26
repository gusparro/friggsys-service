package com.gusparro.friggsys.adapter.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class BadResquestError extends AdapterException {

    private final String entityName;
    private final String action;
    private final String field;

    public BadResquestError(String entityName, String action, String field, Map<String, Object> details) {
        var message = String.format("It is not possible to execute '%s' on %s due a problem with the '%s' field",
                action, entityName, field);

        super(message, details);

        this.entityName = entityName;
        this.action = action;
        this.field = field;
    }

    public BadResquestError(String message,
                            String entityName,
                            String action,
                            String field,
                            Map<String, Object> details) {
        super(message, details);

        this.entityName = entityName;
        this.action = action;
        this.field = field;
    }

}
