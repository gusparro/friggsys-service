package com.gusparro.friggsys.usecase.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class EntityNotFoundError extends UseCaseException {

    private final String entityName;
    private final String identifierType;
    private final String identifier;

    public EntityNotFoundError(String entityName, String identifierType, String identifier, Map<String, Object> details) {
        super(String.format("%s with '%s' '%s' not found", entityName, identifierType, identifier), details);

        this.entityName = entityName;
        this.identifierType = identifierType;
        this.identifier = identifier;
    }

}
