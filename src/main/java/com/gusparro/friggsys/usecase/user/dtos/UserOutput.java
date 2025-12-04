package com.gusparro.friggsys.usecase.user.dtos;

import com.gusparro.friggsys.domain.entities.User;
import com.gusparro.friggsys.domain.enums.UserStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserOutput(
        UUID id,
        String name,
        String email,
        String telephone,
        UserStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static UserOutput from(User user) {
        return new UserOutput(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getTelephone(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}