package com.gusparro.friggsys.adapter.api.response;

import com.gusparro.friggsys.usecase.user.dtos.UserOutput;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        String telephone,
        String status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static UserResponse from(UserOutput output) {
        return new UserResponse(
                output.id(),
                output.name(),
                output.email(),
                output.telephone(),
                output.status().getDescription(),
                output.createdAt(),
                output.updatedAt()
        );
    }
}
