package com.gusparro.friggsys.domain.enums;

import lombok.Getter;

@Getter
public enum UserStatus {

    ACTIVE("Active"),
    INACTIVE("Inactive"),
    BLOCKED("Blocked");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }

}