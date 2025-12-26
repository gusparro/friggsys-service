package com.gusparro.friggsys.adapter.exceptions.handler;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)

@Builder
public record ProblemDetails(
        String title,

        Integer status,

        String detail,

        String instance,

        List<FieldValidationDetail> fields,

        @JsonAnyGetter
        Map<String, Object> additionalProperties
) {
    public static ProblemDetails buildBodyResponse(ProblemType problemType,
                                                   String message,
                                                   String instance,
                                                   List<FieldValidationDetail> fields,
                                                   Map<String, Object> additionalProperties) {
        return ProblemDetails.builder()
                .title(problemType.getTitle())
                .status(problemType.getStatus())
                .detail(message)
                .instance(instance)
                .fields(fields)
                .additionalProperties(additionalProperties)
                .build();
    }
}
