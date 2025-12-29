package com.gusparro.friggsys.adapter.api.mappers;

import com.gusparro.friggsys.adapter.api.request.ChangePasswordRequest;
import com.gusparro.friggsys.adapter.api.request.CreateUserRequest;
import com.gusparro.friggsys.adapter.api.request.UpdateUserRequest;
import com.gusparro.friggsys.usecase.user.dtos.ChangePasswordInput;
import com.gusparro.friggsys.usecase.user.dtos.CreateUserInput;
import com.gusparro.friggsys.usecase.user.dtos.UpdateUserInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserApiMapper Tests")
class UserApiMapperTest {

    private UserApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserApiMapper();
    }

    @Test
    @DisplayName("Should map CreateUserRequest to CreateUserInput")
    void shouldMapCreateUserRequestToCreateUserInput() {
        var request = new CreateUserRequest(
                "John Doe",
                "john.doe@example.com",
                "(11) 98888-8888",
                "SecureP@ss123"
        );

        var input = mapper.toCreateUserInput(request);

        assertNotNull(input);
        assertEquals("John Doe", input.name());
        assertEquals("john.doe@example.com", input.email());
        assertEquals("(11) 98888-8888", input.telephone());
        assertEquals("SecureP@ss123", input.password());
    }

    @Test
    @DisplayName("Should map CreateUserRequest with special characters in name")
    void shouldMapCreateUserRequestWithSpecialCharactersInName() {
        var request = new CreateUserRequest(
                "José da Silva",
                "jose@example.com",
                "(11) 99999-9999",
                "Password123"
        );

        var input = mapper.toCreateUserInput(request);

        assertEquals("José da Silva", input.name());
    }

    @Test
    @DisplayName("Should map CreateUserRequest with complex email")
    void shouldMapCreateUserRequestWithComplexEmail() {
        var request = new CreateUserRequest(
                "Jane Smith",
                "jane.smith+test@company.co.uk",
                "(11) 98888-8888",
                "Pass@123"
        );

        var input = mapper.toCreateUserInput(request);

        assertEquals("jane.smith+test@company.co.uk", input.email());
    }

    @Test
    @DisplayName("Should map UpdateUserRequest to UpdateUserInput with UUID")
    void shouldMapUpdateUserRequestToUpdateUserInputWithUuid() {
        UUID userId = UUID.randomUUID();
        var request = new UpdateUserRequest(
                "Jane Updated",
                "jane.updated@example.com",
                "(11) 97777-7777"
        );

        var input = mapper.toUpdateUserInput(userId, request);

        assertNotNull(input);
        assertEquals(userId, input.id());
        assertEquals("Jane Updated", input.name());
        assertEquals("jane.updated@example.com", input.email());
        assertEquals("(11) 97777-7777", input.telephone());
    }

    @Test
    @DisplayName("Should preserve UUID in UpdateUserInput mapping")
    void shouldPreserveUuidInUpdateUserInputMapping() {
        UUID specificId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        var request = new UpdateUserRequest(
                "Test User",
                "test@example.com",
                "(11) 98888-8888"
        );

        var input = mapper.toUpdateUserInput(specificId, request);

        assertEquals(specificId, input.id());
        assertEquals("550e8400-e29b-41d4-a716-446655440000", input.id().toString());
    }

    @Test
    @DisplayName("Should map UpdateUserRequest with all fields")
    void shouldMapUpdateUserRequestWithAllFields() {
        UUID userId = UUID.randomUUID();
        var request = new UpdateUserRequest(
                "Complete Name",
                "complete@email.com",
                "(11) 91111-1111"
        );

        var input = mapper.toUpdateUserInput(userId, request);

        assertNotNull(input.id());
        assertNotNull(input.name());
        assertNotNull(input.email());
        assertNotNull(input.telephone());
    }

    @Test
    @DisplayName("Should map ChangePasswordRequest to ChangePasswordInput with UUID")
    void shouldMapChangePasswordRequestToChangePasswordInputWithUuid() {
        UUID userId = UUID.randomUUID();
        var request = new ChangePasswordRequest(
                "OldPassword123",
                "NewPassword456"
        );

        var input = mapper.toChangePasswordInput(userId, request);

        assertNotNull(input);
        assertEquals(userId, input.id());
        assertEquals("OldPassword123", input.currentPassword());
        assertEquals("NewPassword456", input.newPassword());
    }

    @Test
    @DisplayName("Should map ChangePasswordRequest with complex passwords")
    void shouldMapChangePasswordRequestWithComplexPasswords() {
        UUID userId = UUID.randomUUID();
        var request = new ChangePasswordRequest(
                "Current@P@ssw0rd!123",
                "N3w$ecur3P@ssw0rd!456"
        );

        var input = mapper.toChangePasswordInput(userId, request);

        assertEquals("Current@P@ssw0rd!123", input.currentPassword());
        assertEquals("N3w$ecur3P@ssw0rd!456", input.newPassword());
    }

    @Test
    @DisplayName("Should preserve UUID in ChangePasswordInput mapping")
    void shouldPreserveUuidInChangePasswordInputMapping() {
        UUID specificId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var request = new ChangePasswordRequest(
                "currentPass",
                "newPass"
        );

        var input = mapper.toChangePasswordInput(specificId, request);

        assertEquals(specificId, input.id());
        assertEquals("123e4567-e89b-12d3-a456-426614174000", input.id().toString());
    }

    @Test
    @DisplayName("Should create distinct CreateUserInput instances")
    void shouldCreateDistinctCreateUserInputInstances() {
        var request1 = new CreateUserRequest("User1", "user1@test.com", "(11) 91111-1111", "pass1");
        var request2 = new CreateUserRequest("User2", "user2@test.com", "(11) 92222-2222", "pass2");

        var input1 = mapper.toCreateUserInput(request1);
        var input2 = mapper.toCreateUserInput(request2);

        assertNotEquals(input1.name(), input2.name());
        assertNotEquals(input1.email(), input2.email());
        assertNotEquals(input1.telephone(), input2.telephone());
        assertNotEquals(input1.password(), input2.password());
    }

    @Test
    @DisplayName("Should create distinct UpdateUserInput instances")
    void shouldCreateDistinctUpdateUserInputInstances() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        var request1 = new UpdateUserRequest("User1", "user1@test.com", "(11) 91111-1111");
        var request2 = new UpdateUserRequest("User2", "user2@test.com", "(11) 92222-2222");

        var input1 = mapper.toUpdateUserInput(id1, request1);
        var input2 = mapper.toUpdateUserInput(id2, request2);

        assertNotEquals(input1.id(), input2.id());
        assertNotEquals(input1.name(), input2.name());
        assertNotEquals(input1.email(), input2.email());
    }

    @Test
    @DisplayName("Should create distinct ChangePasswordInput instances")
    void shouldCreateDistinctChangePasswordInputInstances() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        var request1 = new ChangePasswordRequest("current1", "new1");
        var request2 = new ChangePasswordRequest("current2", "new2");

        var input1 = mapper.toChangePasswordInput(id1, request1);
        var input2 = mapper.toChangePasswordInput(id2, request2);

        assertNotEquals(input1.id(), input2.id());
        assertNotEquals(input1.currentPassword(), input2.currentPassword());
        assertNotEquals(input1.newPassword(), input2.newPassword());
    }

    @Test
    @DisplayName("Should map CreateUserRequest with minimum valid data")
    void shouldMapCreateUserRequestWithMinimumValidData() {
        var request = new CreateUserRequest("A", "a@b.c", "1", "p");

        var input = mapper.toCreateUserInput(request);

        assertEquals("A", input.name());
        assertEquals("a@b.c", input.email());
        assertEquals("1", input.telephone());
        assertEquals("p", input.password());
    }

    @Test
    @DisplayName("Should map UpdateUserRequest with minimum valid data")
    void shouldMapUpdateUserRequestWithMinimumValidData() {
        UUID userId = UUID.randomUUID();
        var request = new UpdateUserRequest("A", "a@b.c", "1");

        var input = mapper.toUpdateUserInput(userId, request);

        assertEquals(userId, input.id());
        assertEquals("A", input.name());
        assertEquals("a@b.c", input.email());
        assertEquals("1", input.telephone());
    }

    @Test
    @DisplayName("Should map ChangePasswordRequest with minimum valid data")
    void shouldMapChangePasswordRequestWithMinimumValidData() {
        UUID userId = UUID.randomUUID();
        var request = new ChangePasswordRequest("c", "n");

        var input = mapper.toChangePasswordInput(userId, request);

        assertEquals(userId, input.id());
        assertEquals("c", input.currentPassword());
        assertEquals("n", input.newPassword());
    }

    @Test
    @DisplayName("Should map CreateUserRequest with long name")
    void shouldMapCreateUserRequestWithLongName() {
        var longName = "A".repeat(255);
        var request = new CreateUserRequest(longName, "test@example.com", "(11) 98888-8888", "password");

        var input = mapper.toCreateUserInput(request);

        assertEquals(longName, input.name());
        assertEquals(255, input.name().length());
    }

    @Test
    @DisplayName("Should map multiple CreateUserRequests sequentially")
    void shouldMapMultipleCreateUserRequestsSequentially() {
        var request1 = new CreateUserRequest("User1", "user1@test.com", "(11) 91111-1111", "pass1");
        var request2 = new CreateUserRequest("User2", "user2@test.com", "(11) 92222-2222", "pass2");
        var request3 = new CreateUserRequest("User3", "user3@test.com", "(11) 93333-3333", "pass3");

        var input1 = mapper.toCreateUserInput(request1);
        var input2 = mapper.toCreateUserInput(request2);
        var input3 = mapper.toCreateUserInput(request3);

        assertEquals("User1", input1.name());
        assertEquals("User2", input2.name());
        assertEquals("User3", input3.name());
    }

    @Test
    @DisplayName("Should map multiple UpdateUserRequests sequentially")
    void shouldMapMultipleUpdateUserRequestsSequentially() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();

        var request1 = new UpdateUserRequest("User1", "user1@test.com", "(11) 91111-1111");
        var request2 = new UpdateUserRequest("User2", "user2@test.com", "(11) 92222-2222");
        var request3 = new UpdateUserRequest("User3", "user3@test.com", "(11) 93333-3333");

        var input1 = mapper.toUpdateUserInput(id1, request1);
        var input2 = mapper.toUpdateUserInput(id2, request2);
        var input3 = mapper.toUpdateUserInput(id3, request3);

        assertEquals(id1, input1.id());
        assertEquals(id2, input2.id());
        assertEquals(id3, input3.id());
    }

    @Test
    @DisplayName("Should be a Spring Component")
    void shouldBeASpringComponent() {
        assertNotNull(mapper.getClass().getAnnotation(org.springframework.stereotype.Component.class));
    }

    @Test
    @DisplayName("Should map telephone with different formats")
    void shouldMapTelephoneWithDifferentFormats() {
        var request1 = new CreateUserRequest("User", "user@test.com", "(11) 98888-8888", "pass");
        var request2 = new CreateUserRequest("User", "user@test.com", "11988888888", "pass");
        var request3 = new CreateUserRequest("User", "user@test.com", "+55 11 98888-8888", "pass");

        var input1 = mapper.toCreateUserInput(request1);
        var input2 = mapper.toCreateUserInput(request2);
        var input3 = mapper.toCreateUserInput(request3);

        assertEquals("(11) 98888-8888", input1.telephone());
        assertEquals("11988888888", input2.telephone());
        assertEquals("+55 11 98888-8888", input3.telephone());
    }

    @Test
    @DisplayName("Should not modify request data during mapping")
    void shouldNotModifyRequestDataDuringMapping() {
        var originalName = "Original Name";
        var originalEmail = "original@example.com";
        var originalTelephone = "(11) 98888-8888";
        var originalPassword = "originalPass";

        var request = new CreateUserRequest(originalName, originalEmail, originalTelephone, originalPassword);
        var input = mapper.toCreateUserInput(request);

        assertEquals(originalName, input.name());
        assertEquals(originalEmail, input.email());
        assertEquals(originalTelephone, input.telephone());
        assertEquals(originalPassword, input.password());
    }

}
