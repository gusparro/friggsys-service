package com.gusparro.friggsys.domain.entities;

import com.gusparro.friggsys.domain.enums.UserStatus;
import com.gusparro.friggsys.domain.exceptions.DomainExceptionFactory;
import com.gusparro.friggsys.domain.vos.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;

@DisplayName("User Entity Tests")
class UserTest {

    private Name name;
    private Email email;
    private Telephone telephone;
    private Password password;
    private UUID userId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @BeforeEach
    void setUp() {
        name = Name.of("Maria Silva");
        email = Email.of("maria@example.com");
        telephone = Telephone.of("(11) 98765-4321");
        password = Password.ofRaw("ValidPass123!");
        userId = UUID.randomUUID();
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @Test
    @DisplayName("Should create user with create method")
    void shouldCreateUserWithCreateMethod() {
        User user = User.create(name, email, telephone, password);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isEqualTo("Maria Silva");
        assertThat(user.getEmail()).isEqualTo("maria@example.com");
        assertThat(user.getTelephone()).isEqualTo("(11) 98765-4321");
        assertThat(user.getPassword()).isEqualTo("ValidPass123!");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should create user with reconstruct method")
    void shouldCreateUserWithReconstructMethod() {
        User user = User.reconstruct(userId, name, email, telephone, password, UserStatus.ACTIVE, createdAt, updatedAt);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userId);
        assertThat(user.getName()).isEqualTo("Maria Silva");
        assertThat(user.getEmail()).isEqualTo("maria@example.com");
        assertThat(user.getTelephone()).isEqualTo("(11) 98765-4321");
        assertThat(user.getPassword()).isEqualTo("ValidPass123!");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getCreatedAt()).isEqualTo(createdAt);
        assertThat(user.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Should set active status when creating new user")
    void shouldSetActiveStatusWhenCreatingNewUser() {
        User user = User.create(name, email, telephone, password);

        assertThat(user.isActive()).isTrue();
        assertThat(user.isInactive()).isFalse();
        assertThat(user.isBlocked()).isFalse();
    }

    @Test
    @DisplayName("Should update user data")
    void shouldUpdateUserData() {
        User user = User.create(name, email, telephone, password);
        Name newName = Name.of("João Pedro");
        Email newEmail = Email.of("joao@example.com");
        Telephone newTelephone = Telephone.of("(21) 98765-4321");

        user.update(newName, newEmail, newTelephone);

        assertThat(user.getName()).isEqualTo("João Pedro");
        assertThat(user.getEmail()).isEqualTo("joao@example.com");
        assertThat(user.getTelephone()).isEqualTo("(21) 98765-4321");
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should change password")
    void shouldChangePassword() {
        User user = User.create(name, email, telephone, password);
        Password newPassword = Password.ofRaw("NewPass456!");

        user.changePassword(newPassword);

        assertThat(user.getPassword()).isEqualTo("NewPass456!");
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should activate user when inactive")
    void shouldActivateUserWhenInactive() {
        User user = User.reconstruct(userId, name, email, telephone, password, UserStatus.INACTIVE, createdAt, updatedAt);

        user.activate();

        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.isActive()).isTrue();
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should throw exception when trying to activate already active user")
    void shouldThrowExceptionWhenTryingToActivateAlreadyActiveUser() {
        User user = User.create(name, email, telephone, password);

        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.invalidState("User", UserStatus.ACTIVE.getDescription(), "activate"))
                    .thenThrow(new RuntimeException("User is already active"));

            assertThatThrownBy(user::activate)
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Test
    @DisplayName("Should deactivate user when active")
    void shouldDeactivateUserWhenActive() {
        User user = User.create(name, email, telephone, password);

        user.deactivate();

        assertThat(user.getStatus()).isEqualTo(UserStatus.INACTIVE);
        assertThat(user.isInactive()).isTrue();
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should throw exception when trying to deactivate already inactive user")
    void shouldThrowExceptionWhenTryingToDeactivateAlreadyInactiveUser() {
        User user = User.reconstruct(userId, name, email, telephone, password, UserStatus.INACTIVE, createdAt, updatedAt);

        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.invalidState("User", UserStatus.INACTIVE.getDescription(), "deactivate"))
                    .thenThrow(new RuntimeException("User is already inactive"));

            assertThatThrownBy(user::deactivate)
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Test
    @DisplayName("Should block user when not blocked")
    void shouldBlockUserWhenNotBlocked() {
        User user = User.create(name, email, telephone, password);

        user.block();

        assertThat(user.getStatus()).isEqualTo(UserStatus.BLOCKED);
        assertThat(user.isBlocked()).isTrue();
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should throw exception when trying to block already blocked user")
    void shouldThrowExceptionWhenTryingToBlockAlreadyBlockedUser() {
        User user = User.reconstruct(userId, name, email, telephone, password, UserStatus.BLOCKED, createdAt, updatedAt);

        try (MockedStatic<DomainExceptionFactory> mockedFactory = mockStatic(DomainExceptionFactory.class)) {
            mockedFactory.when(() -> DomainExceptionFactory.invalidState("User", UserStatus.BLOCKED.getDescription(), "block"))
                    .thenThrow(new RuntimeException("User is already blocked"));

            assertThatThrownBy(user::block)
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Test
    @DisplayName("Should return user name")
    void shouldReturnUserName() {
        User user = User.create(name, email, telephone, password);

        assertThat(user.getName()).isEqualTo("Maria Silva");
    }

    @Test
    @DisplayName("Should return user email")
    void shouldReturnUserEmail() {
        User user = User.create(name, email, telephone, password);

        assertThat(user.getEmail()).isEqualTo("maria@example.com");
    }

    @Test
    @DisplayName("Should return user telephone")
    void shouldReturnUserTelephone() {
        User user = User.create(name, email, telephone, password);

        assertThat(user.getTelephone()).isEqualTo("(11) 98765-4321");
    }

    @Test
    @DisplayName("Should return user password")
    void shouldReturnUserPassword() {
        User user = User.create(name, email, telephone, password);

        assertThat(user.getPassword()).isEqualTo("ValidPass123!");
    }

    @Test
    @DisplayName("Should return user status")
    void shouldReturnUserStatus() {
        User user = User.create(name, email, telephone, password);

        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should return user ID")
    void shouldReturnUserId() {
        UUID id = UUID.randomUUID();
        User user = User.reconstruct(id, name, email, telephone, password, UserStatus.ACTIVE, createdAt, updatedAt);

        assertThat(user.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Should return user created at")
    void shouldReturnUserCreatedAt() {
        User user = User.reconstruct(userId, name, email, telephone, password, UserStatus.ACTIVE, createdAt, updatedAt);

        assertThat(user.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Should return user updated at")
    void shouldReturnUserUpdatedAt() {
        User user = User.reconstruct(userId, name, email, telephone, password, UserStatus.ACTIVE, createdAt, updatedAt);

        assertThat(user.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Should check if user is active")
    void shouldCheckIfUserIsActive() {
        User user = User.create(name, email, telephone, password);

        assertThat(user.isActive()).isTrue();
    }

    @Test
    @DisplayName("Should check if user is inactive")
    void shouldCheckIfUserIsInactive() {
        User user = User.reconstruct(userId, name, email, telephone, password, UserStatus.INACTIVE, createdAt, updatedAt);

        assertThat(user.isInactive()).isTrue();
    }

    @Test
    @DisplayName("Should check if user is blocked")
    void shouldCheckIfUserIsBlocked() {
        User user = User.reconstruct(userId, name, email, telephone, password, UserStatus.BLOCKED, createdAt, updatedAt);

        assertThat(user.isBlocked()).isTrue();
    }

    @Test
    @DisplayName("Should be equal to another user with same id")
    void shouldBeEqualToAnotherUserWithSameId() {
        UUID id = UUID.randomUUID();
        User user1 = User.reconstruct(id, name, email, telephone, password, UserStatus.ACTIVE, createdAt, updatedAt);
        User user2 = User.reconstruct(id, name, email, telephone, password, UserStatus.ACTIVE, createdAt, updatedAt);

        assertThat(user1).isEqualTo(user2);
    }

    @Test
    @DisplayName("Should have same hashcode for equal users")
    void shouldHaveSameHashCodeForEqualUsers() {
        UUID id = UUID.randomUUID();
        User user1 = User.reconstruct(id, name, email, telephone, password, UserStatus.ACTIVE, createdAt, updatedAt);
        User user2 = User.reconstruct(id, name, email, telephone, password, UserStatus.ACTIVE, createdAt, updatedAt);

        assertThat(user1).hasSameHashCodeAs(user2);
    }

    @Test
    @DisplayName("Should not be equal to another user with different id")
    void shouldNotBeEqualToAnotherUserWithDifferentId() {
        User user1 = User.reconstruct(UUID.randomUUID(), name, email, telephone, password, UserStatus.ACTIVE, createdAt, updatedAt);
        User user2 = User.reconstruct(UUID.randomUUID(), name, email, telephone, password, UserStatus.ACTIVE, createdAt, updatedAt);

        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    @DisplayName("Should transition from active to inactive to blocked")
    void shouldTransitionFromActiveToInactiveToBlocked() {
        User user = User.create(name, email, telephone, password);

        assertThat(user.isActive()).isTrue();

        user.deactivate();
        assertThat(user.isInactive()).isTrue();

        user.activate();
        assertThat(user.isActive()).isTrue();

        user.block();
        assertThat(user.isBlocked()).isTrue();
    }

    @Test
    @DisplayName("Should update timestamp when user data changes")
    void shouldUpdateTimestampWhenUserDataChanges() {
        User user = User.create(name, email, telephone, password);
        OffsetDateTime initialUpdatedAt = user.getUpdatedAt();

        user.update(Name.of("Novo Nome"), Email.of("novo@example.com"), Telephone.of("(21) 98765-4321"));

        assertThat(user.getUpdatedAt()).isAfterOrEqualTo(initialUpdatedAt);
    }

}