package com.gusparro.friggsys.adapter.exceptions.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Arrays;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ProblemType {

    IS_BAD_REQUEST_ERROR(BAD_REQUEST),
    IS_NOT_FOUND_ERROR(NOT_FOUND),
    IS_CONFLICT_ERROR(CONFLICT),
    IS_INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR);

    private final Integer status;
    private final String title;

    ProblemType(HttpStatus status) {
        this.status = status.value();
        this.title = status.getReasonPhrase();
    }

    public static ProblemType fromStatusCode(HttpStatusCode statusCode) {
        if (statusCode instanceof HttpStatus httpStatus) {
            return Arrays.stream(values())
                    .filter(pt -> pt.status == httpStatus.value())
                    .findFirst()
                    .orElse(IS_INTERNAL_SERVER_ERROR);
        }

        return IS_INTERNAL_SERVER_ERROR;
    }
}