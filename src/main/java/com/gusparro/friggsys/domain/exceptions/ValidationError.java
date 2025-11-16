package com.gusparro.friggsys.domain.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class ValidationError extends DomainException {

    private final String field;

    public ValidationError(String message) {
        this(message, null, null);
    }

    public ValidationError(String message, String field) {
        this(message, field, null);
    }

    public ValidationError(String message, String field, Map<String, Object> details) {
        super(message, details);

        this.field = field;
    }

}
