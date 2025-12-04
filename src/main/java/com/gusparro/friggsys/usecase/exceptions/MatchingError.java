package com.gusparro.friggsys.usecase.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class MatchingError extends UseCaseException {

    private final String entityName;
    private final String fieldName;

    public MatchingError(String entityName, String fieldName, Map<String, Object> details) {
        super(String.format("'%s' failed to match the %s' field.", entityName, fieldName), details);

        this.entityName = entityName;
        this.fieldName = fieldName;
    }

}
