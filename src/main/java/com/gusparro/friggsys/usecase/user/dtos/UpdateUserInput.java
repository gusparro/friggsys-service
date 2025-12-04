package com.gusparro.friggsys.usecase.user.dtos;

import java.util.UUID;

public record UpdateUserInput(
        UUID id,
        String name,
        String email,
        String telephone
) {}
