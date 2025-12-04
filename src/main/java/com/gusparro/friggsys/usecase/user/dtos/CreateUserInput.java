package com.gusparro.friggsys.usecase.user.dtos;

public record CreateUserInput(
        String name,
        String email,
        String telephone,
        String password
) {}
