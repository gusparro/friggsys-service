package com.gusparro.friggsys.adapter.exceptions.handler;

import lombok.Builder;

@Builder
public record FieldValidationDetail(
        String name,
        String message
) {
}
