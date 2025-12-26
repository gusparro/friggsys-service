package com.gusparro.friggsys.adapter.exceptions;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class AdapterException extends RuntimeException {

    private final Map<String, Object> details;

    public AdapterException(String message) {
        this(message, new HashMap<>());
    }

    public AdapterException(String message, Throwable cause) {
        this(message, new HashMap<>(), cause);
    }

    public AdapterException(String message, Map<String, Object> details) {
        super(message);

        this.details = details != null ? details : new HashMap<>();
    }

    public AdapterException(String message, Map<String, Object> details, Throwable cause) {
        super(message, cause);

        this.details = details != null ? details : new HashMap<>();
    }

    public void addDetail(String key, Object value) {
        details.put(key, value);
    }

}
