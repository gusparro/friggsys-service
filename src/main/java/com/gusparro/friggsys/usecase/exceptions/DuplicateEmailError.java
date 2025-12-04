package com.gusparro.friggsys.usecase.exceptions;

import java.util.Map;

public class DuplicateEmailError extends UseCaseException {

    public DuplicateEmailError(String message, Map<String, Object> details) {
        super(message, details);
    }

}
