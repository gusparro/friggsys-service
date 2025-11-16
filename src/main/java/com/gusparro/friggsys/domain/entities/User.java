package com.gusparro.friggsys.domain.entities;

import com.gusparro.friggsys.domain.enums.UserStatus;
import com.gusparro.friggsys.domain.exceptions.DomainExceptionFactory;
import com.gusparro.friggsys.domain.vos.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class User {

    private static final Logger logger = LoggerFactory.getLogger(User.class);

    @Getter
    private UUID id;

    private Name name;

    private Email email;

    private Telephone telephone;

    private Password password;

    @Getter
    private UserStatus status;

    @Getter
    private OffsetDateTime createdAt;

    @Getter
    private OffsetDateTime updatedAt;

    public static User create(Name name,
                              Email email,
                              Telephone telephone,
                              Password password) {
        return new User(
                null,
                name,
                email,
                telephone,
                password,
                UserStatus.ACTIVE,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
    }

    public static User reconstruct(UUID id,
                                   Name name,
                                   Email email,
                                   Telephone telephone,
                                   Password password,
                                   UserStatus status,
                                   OffsetDateTime createdAt,
                                   OffsetDateTime updatedAt) {
        return new User(
                id,
                name,
                email,
                telephone,
                password,
                status,
                createdAt,
                updatedAt
        );
    }

    public void update(Name name,
                       Email email,
                       Telephone telephone) {
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.updatedAt = OffsetDateTime.now();
    }

    public void changePassword(Password newPassword) {
        this.password = newPassword;
        this.updatedAt = OffsetDateTime.now();
    }

    public void activate() {
        if (isActive()) {
            logger.error("User is already active.");

            throw DomainExceptionFactory.invalidState("User", status.getDescription(), "activate");
        }

        status = UserStatus.ACTIVE;
        this.updatedAt = OffsetDateTime.now();
    }

    public void deactivate() {
        if (isInactive()) {
            logger.error("User is already inactive.");

            throw DomainExceptionFactory.invalidState("User", status.getDescription(), "deactivate");
        }

        status = UserStatus.INACTIVE;
        this.updatedAt = OffsetDateTime.now();
    }

    public void block() {
        if (isBlocked()) {
            logger.error("User is already blocked.");

            throw DomainExceptionFactory.invalidState("User", status.getDescription(), "block");
        }

        status = UserStatus.BLOCKED;
        this.updatedAt = OffsetDateTime.now();
    }

    public String getName() {
        return name.getValue();
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getTelephone() {
        return telephone.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }

    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }

    public boolean isInactive() {
        return status == UserStatus.INACTIVE;
    }

    public boolean isBlocked() {
        return status == UserStatus.BLOCKED;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
