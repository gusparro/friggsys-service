package com.gusparro.friggsys.adapter.api.response;

import com.gusparro.friggsys.usecase.user.dtos.UserOutput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UserResponse Tests")
class UserResponseTest {

    @Test
    @DisplayName("Should create UserResponse with all fields")
    void shouldCreateUserResponseWithAllFields() {
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        var response = new UserResponse(
                id,
                "John Doe",
                "john@example.com",
                "(11) 98888-8888",
                "active",
                now,
                now
        );

        assertEquals(id, response.id());
        assertEquals("John Doe", response.name());
        assertEquals("john@example.com", response.email());
        assertEquals("(11) 98888-8888", response.telephone());
        assertEquals("active", response.status());
        assertEquals(now, response.createdAt());
        assertEquals(now, response.updatedAt());
    }

    @Test
    @DisplayName("Should create UserResponse from UserOutput")
    void shouldCreateUserResponseFromUserOutput() {
        UUID id = UUID.randomUUID();
        OffsetDateTime createdAt = OffsetDateTime.now(ZoneOffset.UTC).minusDays(1);
        OffsetDateTime updatedAt = OffsetDateTime.now(ZoneOffset.UTC);

        var userStatus = mock(com.gusparro.friggsys.domain.enums.UserStatus.class);
        when(userStatus.getDescription()).thenReturn("Active");

        var output = mock(UserOutput.class);
        when(output.id()).thenReturn(id);
        when(output.name()).thenReturn("Jane Smith");
        when(output.email()).thenReturn("jane@example.com");
        when(output.telephone()).thenReturn("(11) 97777-7777");
        when(output.status()).thenReturn(userStatus);
        when(output.createdAt()).thenReturn(createdAt);
        when(output.updatedAt()).thenReturn(updatedAt);

        var response = UserResponse.from(output);

        assertEquals(id, response.id());
        assertEquals("Jane Smith", response.name());
        assertEquals("jane@example.com", response.email());
        assertEquals("(11) 97777-7777", response.telephone());
        assertEquals("Active", response.status());
        assertEquals(createdAt, response.createdAt());
        assertEquals(updatedAt, response.updatedAt());
    }

    @Test
    @DisplayName("Should map status description from UserOutput")
    void shouldMapStatusDescriptionFromUserOutput() {
        var userStatus = mock(com.gusparro.friggsys.domain.enums.UserStatus.class);
        when(userStatus.getDescription()).thenReturn("Inactive");

        var output = mock(UserOutput.class);
        when(output.id()).thenReturn(UUID.randomUUID());
        when(output.name()).thenReturn("Test");
        when(output.email()).thenReturn("test@test.com");
        when(output.telephone()).thenReturn("123456");
        when(output.status()).thenReturn(userStatus);
        when(output.createdAt()).thenReturn(OffsetDateTime.now());
        when(output.updatedAt()).thenReturn(OffsetDateTime.now());

        var response = UserResponse.from(output);

        assertEquals("Inactive", response.status());
        verify(userStatus, times(1)).getDescription();
    }

    @Test
    @DisplayName("Should preserve UUID in response")
    void shouldPreserveUuidInResponse() {
        UUID specificId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

        var response = new UserResponse(
                specificId,
                "Test User",
                "test@test.com",
                "123456",
                "active",
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        assertEquals(specificId, response.id());
        assertEquals("550e8400-e29b-41d4-a716-446655440000", response.id().toString());
    }

    @Test
    @DisplayName("Should preserve timestamps in response")
    void shouldPreserveTimestampsInResponse() {
        OffsetDateTime createdAt = OffsetDateTime.of(2024, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime updatedAt = OffsetDateTime.of(2024, 1, 15, 15, 30, 0, 0, ZoneOffset.UTC);

        var response = new UserResponse(
                UUID.randomUUID(),
                "User",
                "user@test.com",
                "123",
                "active",
                createdAt,
                updatedAt
        );

        assertEquals(createdAt, response.createdAt());
        assertEquals(updatedAt, response.updatedAt());
        assertTrue(response.updatedAt().isAfter(response.createdAt()));
    }

    @Test
    @DisplayName("Should create response with different status values")
    void shouldCreateResponseWithDifferentStatusValues() {
        var activeResponse = new UserResponse(
                UUID.randomUUID(), "User1", "user1@test.com", "123", "active",
                OffsetDateTime.now(), OffsetDateTime.now()
        );

        var inactiveResponse = new UserResponse(
                UUID.randomUUID(), "User2", "user2@test.com", "456", "inactive",
                OffsetDateTime.now(), OffsetDateTime.now()
        );

        var blockedResponse = new UserResponse(
                UUID.randomUUID(), "User3", "user3@test.com", "789", "blocked",
                OffsetDateTime.now(), OffsetDateTime.now()
        );

        assertEquals("active", activeResponse.status());
        assertEquals("inactive", inactiveResponse.status());
        assertEquals("blocked", blockedResponse.status());
    }

    @Test
    @DisplayName("Should be a record type")
    void shouldBeARecordType() {
        assertTrue(UserResponse.class.isRecord());
    }

    @Test
    @DisplayName("Should have correct record components")
    void shouldHaveCorrectRecordComponents() {
        var components = UserResponse.class.getRecordComponents();

        assertEquals(7, components.length);
        assertEquals("id", components[0].getName());
        assertEquals("name", components[1].getName());
        assertEquals("email", components[2].getName());
        assertEquals("telephone", components[3].getName());
        assertEquals("status", components[4].getName());
        assertEquals("createdAt", components[5].getName());
        assertEquals("updatedAt", components[6].getName());
    }

    @Test
    @DisplayName("Should create equal responses with same data")
    void shouldCreateEqualResponsesWithSameData() {
        UUID id = UUID.randomUUID();
        OffsetDateTime timestamp = OffsetDateTime.now();

        var response1 = new UserResponse(
                id, "User", "user@test.com", "123", "active", timestamp, timestamp
        );

        var response2 = new UserResponse(
                id, "User", "user@test.com", "123", "active", timestamp, timestamp
        );

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    @DisplayName("Should create different responses with different data")
    void shouldCreateDifferentResponsesWithDifferentData() {
        OffsetDateTime timestamp = OffsetDateTime.now();

        var response1 = new UserResponse(
                UUID.randomUUID(), "User1", "user1@test.com", "123", "active", timestamp, timestamp
        );

        var response2 = new UserResponse(
                UUID.randomUUID(), "User2", "user2@test.com", "456", "active", timestamp, timestamp
        );

        assertNotEquals(response1, response2);
    }

    @Test
    @DisplayName("Should generate toString with all fields")
    void shouldGenerateToStringWithAllFields() {
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        var response = new UserResponse(
                id, "Test User", "test@test.com", "123456", "active", now, now
        );

        String toString = response.toString();

        assertTrue(toString.contains("id=" + id));
        assertTrue(toString.contains("name=Test User"));
        assertTrue(toString.contains("email=test@test.com"));
        assertTrue(toString.contains("telephone=123456"));
        assertTrue(toString.contains("status=active"));
    }

    @Test
    @DisplayName("Should map UserOutput with special characters in name")
    void shouldMapUserOutputWithSpecialCharactersInName() {
        var userStatus = mock(com.gusparro.friggsys.domain.enums.UserStatus.class);
        when(userStatus.getDescription()).thenReturn("Active");

        var output = mock(UserOutput.class);
        when(output.id()).thenReturn(UUID.randomUUID());
        when(output.name()).thenReturn("José da Silva");
        when(output.email()).thenReturn("jose@example.com");
        when(output.telephone()).thenReturn("(11) 98888-8888");
        when(output.status()).thenReturn(userStatus);
        when(output.createdAt()).thenReturn(OffsetDateTime.now());
        when(output.updatedAt()).thenReturn(OffsetDateTime.now());

        var response = UserResponse.from(output);

        assertEquals("José da Silva", response.name());
    }

    @Test
    @DisplayName("Should map UserOutput with complex email")
    void shouldMapUserOutputWithComplexEmail() {
        var userStatus = mock(com.gusparro.friggsys.domain.enums.UserStatus.class);
        when(userStatus.getDescription()).thenReturn("Active");

        var output = mock(UserOutput.class);
        when(output.id()).thenReturn(UUID.randomUUID());
        when(output.name()).thenReturn("User");
        when(output.email()).thenReturn("user+tag@company.co.uk");
        when(output.telephone()).thenReturn("123");
        when(output.status()).thenReturn(userStatus);
        when(output.createdAt()).thenReturn(OffsetDateTime.now());
        when(output.updatedAt()).thenReturn(OffsetDateTime.now());

        var response = UserResponse.from(output);

        assertEquals("user+tag@company.co.uk", response.email());
    }

    @Test
    @DisplayName("Should map UserOutput with different telephone formats")
    void shouldMapUserOutputWithDifferentTelephoneFormats() {
        var userStatus = mock(com.gusparro.friggsys.domain.enums.UserStatus.class);
        when(userStatus.getDescription()).thenReturn("Active");

        var output = mock(UserOutput.class);
        when(output.id()).thenReturn(UUID.randomUUID());
        when(output.name()).thenReturn("User");
        when(output.email()).thenReturn("user@test.com");
        when(output.telephone()).thenReturn("+55 11 98888-8888");
        when(output.status()).thenReturn(userStatus);
        when(output.createdAt()).thenReturn(OffsetDateTime.now());
        when(output.updatedAt()).thenReturn(OffsetDateTime.now());

        var response = UserResponse.from(output);

        assertEquals("+55 11 98888-8888", response.telephone());
    }

    @Test
    @DisplayName("Should call UserOutput methods only once when creating response")
    void shouldCallUserOutputMethodsOnlyOnceWhenCreatingResponse() {
        var userStatus = mock(com.gusparro.friggsys.domain.enums.UserStatus.class);
        when(userStatus.getDescription()).thenReturn("Active");

        var output = mock(UserOutput.class);
        when(output.id()).thenReturn(UUID.randomUUID());
        when(output.name()).thenReturn("User");
        when(output.email()).thenReturn("user@test.com");
        when(output.telephone()).thenReturn("123");
        when(output.status()).thenReturn(userStatus);
        when(output.createdAt()).thenReturn(OffsetDateTime.now());
        when(output.updatedAt()).thenReturn(OffsetDateTime.now());

        UserResponse.from(output);

        verify(output, times(1)).id();
        verify(output, times(1)).name();
        verify(output, times(1)).email();
        verify(output, times(1)).telephone();
        verify(output, times(1)).status();
        verify(output, times(1)).createdAt();
        verify(output, times(1)).updatedAt();
    }

    @Test
    @DisplayName("Should create multiple distinct responses from different outputs")
    void shouldCreateMultipleDistinctResponsesFromDifferentOutputs() {
        var status1 = mock(com.gusparro.friggsys.domain.enums.UserStatus.class);
        when(status1.getDescription()).thenReturn("Active");

        var status2 = mock(com.gusparro.friggsys.domain.enums.UserStatus.class);
        when(status2.getDescription()).thenReturn("Inactive");

        var output1 = mock(UserOutput.class);
        when(output1.id()).thenReturn(UUID.randomUUID());
        when(output1.name()).thenReturn("User1");
        when(output1.email()).thenReturn("user1@test.com");
        when(output1.telephone()).thenReturn("111");
        when(output1.status()).thenReturn(status1);
        when(output1.createdAt()).thenReturn(OffsetDateTime.now());
        when(output1.updatedAt()).thenReturn(OffsetDateTime.now());

        var output2 = mock(UserOutput.class);
        when(output2.id()).thenReturn(UUID.randomUUID());
        when(output2.name()).thenReturn("User2");
        when(output2.email()).thenReturn("user2@test.com");
        when(output2.telephone()).thenReturn("222");
        when(output2.status()).thenReturn(status2);
        when(output2.createdAt()).thenReturn(OffsetDateTime.now());
        when(output2.updatedAt()).thenReturn(OffsetDateTime.now());

        var response1 = UserResponse.from(output1);
        var response2 = UserResponse.from(output2);

        assertNotEquals(response1, response2);
        assertNotEquals(response1.id(), response2.id());
        assertNotEquals(response1.name(), response2.name());
        assertNotEquals(response1.status(), response2.status());
    }

    @Test
    @DisplayName("Should handle timestamps with different timezones")
    void shouldHandleTimestampsWithDifferentTimezones() {
        OffsetDateTime utcTime = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime brTime = OffsetDateTime.now(ZoneOffset.ofHours(-3));

        var response1 = new UserResponse(
                UUID.randomUUID(), "User", "user@test.com", "123", "active", utcTime, utcTime
        );

        var response2 = new UserResponse(
                UUID.randomUUID(), "User", "user@test.com", "123", "active", brTime, brTime
        );

        assertNotEquals(response1.createdAt().getOffset(), response2.createdAt().getOffset());
    }

}