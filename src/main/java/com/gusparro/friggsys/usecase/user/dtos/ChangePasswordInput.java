package com.gusparro.friggsys.usecase.user.dtos;

import java.util.UUID;

public record ChangePasswordInput(
        UUID id,
        String currentPassword,
        String newPassword
) {}
