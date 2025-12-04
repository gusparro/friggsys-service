package com.gusparro.friggsys.usecase.exceptions;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class UseCaseException extends RuntimeException {

    private final Map<String, Object> details;

    public UseCaseException(String message) {
        this(message, new HashMap<>());
    }

    public UseCaseException(String message, Throwable cause) {
        this(message, new HashMap<>(), cause);
    }

    public UseCaseException(String message, Map<String, Object> details) {
        super(message);

        this.details = details != null ? details : new HashMap<>();
    }

    public UseCaseException(String message, Map<String, Object> details, Throwable cause) {
        super(message, cause);

        this.details = details != null ? details : new HashMap<>();
    }

    public void addDetail(String key, Object value) {
        details.put(key, value);
    }

}
