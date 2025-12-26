package com.gusparro.friggsys.adapter.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class NotFoundError extends AdapterException {

    private final String entityName;
    private final String fieldName;

    public NotFoundError(String entityName, String fieldName, Map<String, Object> details) {
        var message = String.format("It is not possible to retrieve '%s' with '%s' equals to the given value",
                entityName, fieldName);

        super(message, details);

        this.entityName = entityName;
        this.fieldName = fieldName;
    }

}
