package com.gusparro.friggsys.adapter.persistence.entities;

import com.gusparro.friggsys.domain.enums.UserStatus;
import jakarta.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserEntity Tests")
class UserEntityTest {

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
    }

    @Test
    @DisplayName("Should create entity with no-args constructor")
    void shouldCreateEntityWithNoArgsConstructor() {
        assertNotNull(userEntity);
        assertNull(userEntity.getId());
        assertNull(userEntity.getName());
        assertNull(userEntity.getEmail());
        assertNull(userEntity.getTelephone());
        assertNull(userEntity.getPasswordHash());
        assertNull(userEntity.getStatus());
        assertNull(userEntity.getCreatedAt());
        assertNull(userEntity.getUpdatedAt());
    }

    @Test
    @DisplayName("Should create entity with all-args constructor")
    void shouldCreateEntityWithAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        var entity = new UserEntity(
                id,
                "John Doe",
                "(11) 98888-8888",
                "john@example.com",
                "hashed_password",
                UserStatus.ACTIVE,
                now,
                now
        );

        assertEquals(id, entity.getId());
        assertEquals("John Doe", entity.getName());
        assertEquals("(11) 98888-8888", entity.getTelephone());
        assertEquals("john@example.com", entity.getEmail());
        assertEquals("hashed_password", entity.getPasswordHash());
        assertEquals(UserStatus.ACTIVE, entity.getStatus());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
    }

    @Test
    @DisplayName("Should set and get id")
    void shouldSetAndGetId() {
        UUID id = UUID.randomUUID();

        userEntity.setId(id);

        assertEquals(id, userEntity.getId());
    }

    @Test
    @DisplayName("Should set and get name")
    void shouldSetAndGetName() {
        userEntity.setName("Jane Smith");

        assertEquals("Jane Smith", userEntity.getName());
    }

    @Test
    @DisplayName("Should set and get telephone")
    void shouldSetAndGetTelephone() {
        userEntity.setTelephone("(11) 97777-7777");

        assertEquals("(11) 97777-7777", userEntity.getTelephone());
    }

    @Test
    @DisplayName("Should set and get email")
    void shouldSetAndGetEmail() {
        userEntity.setEmail("user@example.com");

        assertEquals("user@example.com", userEntity.getEmail());
    }

    @Test
    @DisplayName("Should set and get passwordHash")
    void shouldSetAndGetPasswordHash() {
        userEntity.setPasswordHash("$2a$10$hashed");

        assertEquals("$2a$10$hashed", userEntity.getPasswordHash());
    }

    @Test
    @DisplayName("Should set and get status")
    void shouldSetAndGetStatus() {
        userEntity.setStatus(UserStatus.ACTIVE);

        assertEquals(UserStatus.ACTIVE, userEntity.getStatus());
    }

    @Test
    @DisplayName("Should set and get createdAt")
    void shouldSetAndGetCreatedAt() {
        OffsetDateTime now = OffsetDateTime.now();

        userEntity.setCreatedAt(now);

        assertEquals(now, userEntity.getCreatedAt());
    }

    @Test
    @DisplayName("Should set and get updatedAt")
    void shouldSetAndGetUpdatedAt() {
        OffsetDateTime now = OffsetDateTime.now();

        userEntity.setUpdatedAt(now);

        assertEquals(now, userEntity.getUpdatedAt());
    }

    @Test
    @DisplayName("Should set timestamps on onCreate")
    void shouldSetTimestampsOnOnCreate() {
        OffsetDateTime before = OffsetDateTime.now();

        userEntity.onCreate();

        OffsetDateTime after = OffsetDateTime.now();

        assertNotNull(userEntity.getCreatedAt());
        assertNotNull(userEntity.getUpdatedAt());
        assertTrue(userEntity.getCreatedAt().isAfter(before.minusSeconds(1)));
        assertTrue(userEntity.getCreatedAt().isBefore(after.plusSeconds(1)));
        assertTrue(userEntity.getUpdatedAt().isAfter(before.minusSeconds(1)));
        assertTrue(userEntity.getUpdatedAt().isBefore(after.plusSeconds(1)));
        assertEquals(userEntity.getCreatedAt(), userEntity.getUpdatedAt());
    }

    @Test
    @DisplayName("Should update updatedAt timestamp on onUpdate")
    void shouldUpdateUpdatedAtTimestampOnOnUpdate() throws InterruptedException {
        OffsetDateTime initialCreatedAt = OffsetDateTime.now();
        userEntity.setCreatedAt(initialCreatedAt);
        userEntity.setUpdatedAt(initialCreatedAt);

        Thread.sleep(10);

        userEntity.onUpdate();

        assertNotNull(userEntity.getUpdatedAt());
        assertTrue(userEntity.getUpdatedAt().isAfter(initialCreatedAt));
        assertEquals(initialCreatedAt, userEntity.getCreatedAt());
    }

    @Test
    @DisplayName("Should not modify createdAt on onUpdate")
    void shouldNotModifyCreatedAtOnOnUpdate() {
        OffsetDateTime initialCreatedAt = OffsetDateTime.now().minusDays(1);
        userEntity.setCreatedAt(initialCreatedAt);

        userEntity.onUpdate();

        assertEquals(initialCreatedAt, userEntity.getCreatedAt());
    }

    @Test
    @DisplayName("Should be annotated with Entity")
    void shouldBeAnnotatedWithEntity() {
        assertNotNull(UserEntity.class.getAnnotation(Entity.class));
    }

    @Test
    @DisplayName("Should be annotated with Table")
    void shouldBeAnnotatedWithTable() {
        Table tableAnnotation = UserEntity.class.getAnnotation(Table.class);
        assertNotNull(tableAnnotation);
        assertEquals("users", tableAnnotation.name());
    }

    @Test
    @DisplayName("Should have Id annotation on id field")
    void shouldHaveIdAnnotationOnIdField() throws NoSuchFieldException {
        var idField = UserEntity.class.getDeclaredField("id");
        assertNotNull(idField.getAnnotation(Id.class));
    }

    @Test
    @DisplayName("Should have GeneratedValue annotation on id field")
    void shouldHaveGeneratedValueAnnotationOnIdField() throws NoSuchFieldException {
        var idField = UserEntity.class.getDeclaredField("id");
        GeneratedValue generatedValue = idField.getAnnotation(GeneratedValue.class);
        assertNotNull(generatedValue);
        assertEquals(GenerationType.UUID, generatedValue.strategy());
    }

    @Test
    @DisplayName("Should have Column annotation on name field with correct attributes")
    void shouldHaveColumnAnnotationOnNameFieldWithCorrectAttributes() throws NoSuchFieldException {
        var nameField = UserEntity.class.getDeclaredField("name");
        Column column = nameField.getAnnotation(Column.class);
        assertNotNull(column);
        assertFalse(column.nullable());
        assertEquals(100, column.length());
    }

    @Test
    @DisplayName("Should have Column annotation on telephone field with correct attributes")
    void shouldHaveColumnAnnotationOnTelephoneFieldWithCorrectAttributes() throws NoSuchFieldException {
        var telephoneField = UserEntity.class.getDeclaredField("telephone");
        Column column = telephoneField.getAnnotation(Column.class);
        assertNotNull(column);
        assertFalse(column.nullable());
        assertEquals(16, column.length());
    }

    @Test
    @DisplayName("Should have Column annotation on email field with unique constraint")
    void shouldHaveColumnAnnotationOnEmailFieldWithUniqueConstraint() throws NoSuchFieldException {
        var emailField = UserEntity.class.getDeclaredField("email");
        Column column = emailField.getAnnotation(Column.class);
        assertNotNull(column);
        assertFalse(column.nullable());
        assertTrue(column.unique());
        assertEquals(100, column.length());
    }

    @Test
    @DisplayName("Should have Column annotation on passwordHash field")
    void shouldHaveColumnAnnotationOnPasswordHashField() throws NoSuchFieldException {
        var passwordField = UserEntity.class.getDeclaredField("passwordHash");
        Column column = passwordField.getAnnotation(Column.class);
        assertNotNull(column);
        assertFalse(column.nullable());
    }

    @Test
    @DisplayName("Should have Enumerated annotation on status field")
    void shouldHaveEnumeratedAnnotationOnStatusField() throws NoSuchFieldException {
        var statusField = UserEntity.class.getDeclaredField("status");
        Enumerated enumerated = statusField.getAnnotation(Enumerated.class);
        assertNotNull(enumerated);
        assertEquals(EnumType.STRING, enumerated.value());
    }

    @Test
    @DisplayName("Should have Column annotation on status field with correct attributes")
    void shouldHaveColumnAnnotationOnStatusFieldWithCorrectAttributes() throws NoSuchFieldException {
        var statusField = UserEntity.class.getDeclaredField("status");
        Column column = statusField.getAnnotation(Column.class);
        assertNotNull(column);
        assertFalse(column.nullable());
        assertEquals(20, column.length());
    }

    @Test
    @DisplayName("Should have Column annotation on createdAt field with updatable false")
    void shouldHaveColumnAnnotationOnCreatedAtFieldWithUpdatableFalse() throws NoSuchFieldException {
        var createdAtField = UserEntity.class.getDeclaredField("createdAt");
        Column column = createdAtField.getAnnotation(Column.class);
        assertNotNull(column);
        assertFalse(column.nullable());
        assertFalse(column.updatable());
    }

    @Test
    @DisplayName("Should have Column annotation on updatedAt field")
    void shouldHaveColumnAnnotationOnUpdatedAtField() throws NoSuchFieldException {
        var updatedAtField = UserEntity.class.getDeclaredField("updatedAt");
        Column column = updatedAtField.getAnnotation(Column.class);
        assertNotNull(column);
        assertFalse(column.nullable());
    }

    @Test
    @DisplayName("Should have PrePersist annotation on onCreate method")
    void shouldHavePrePersistAnnotationOnOnCreateMethod() throws NoSuchMethodException {
        var onCreateMethod = UserEntity.class.getDeclaredMethod("onCreate");
        assertNotNull(onCreateMethod.getAnnotation(PrePersist.class));
    }

    @Test
    @DisplayName("Should have PreUpdate annotation on onUpdate method")
    void shouldHavePreUpdateAnnotationOnOnUpdateMethod() throws NoSuchMethodException {
        var onUpdateMethod = UserEntity.class.getDeclaredMethod("onUpdate");
        assertNotNull(onUpdateMethod.getAnnotation(PreUpdate.class));
    }

    @Test
    @DisplayName("Should handle different UserStatus values")
    void shouldHandleDifferentUserStatusValues() {
        userEntity.setStatus(UserStatus.ACTIVE);
        assertEquals(UserStatus.ACTIVE, userEntity.getStatus());

        userEntity.setStatus(UserStatus.INACTIVE);
        assertEquals(UserStatus.INACTIVE, userEntity.getStatus());

        userEntity.setStatus(UserStatus.BLOCKED);
        assertEquals(UserStatus.BLOCKED, userEntity.getStatus());
    }

    @Test
    @DisplayName("Should handle special characters in name")
    void shouldHandleSpecialCharactersInName() {
        userEntity.setName("José da Silva");

        assertEquals("José da Silva", userEntity.getName());
    }

    @Test
    @DisplayName("Should handle complex email formats")
    void shouldHandleComplexEmailFormats() {
        userEntity.setEmail("user+tag@subdomain.example.com");

        assertEquals("user+tag@subdomain.example.com", userEntity.getEmail());
    }

    @Test
    @DisplayName("Should handle different telephone formats")
    void shouldHandleDifferentTelephoneFormats() {
        userEntity.setTelephone("+55 11 98888-8888");

        assertEquals("+55 11 98888-8888", userEntity.getTelephone());
    }

    @Test
    @DisplayName("Should allow null values for nullable fields in construction")
    void shouldAllowNullValuesForNullableFieldsInConstruction() {
        var entity = new UserEntity(
                null, null, null, null, null, null, null, null
        );

        assertNull(entity.getId());
        assertNull(entity.getName());
        assertNull(entity.getTelephone());
        assertNull(entity.getEmail());
        assertNull(entity.getPasswordHash());
        assertNull(entity.getStatus());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
    }

    @Test
    @DisplayName("Should maintain data integrity after multiple updates")
    void shouldMaintainDataIntegrityAfterMultipleUpdates() {
        userEntity.setName("Original Name");
        userEntity.setEmail("original@example.com");

        assertEquals("Original Name", userEntity.getName());
        assertEquals("original@example.com", userEntity.getEmail());

        userEntity.setName("Updated Name");
        userEntity.setEmail("updated@example.com");

        assertEquals("Updated Name", userEntity.getName());
        assertEquals("updated@example.com", userEntity.getEmail());
    }

    @Test
    @DisplayName("Should handle long password hashes")
    void shouldHandleLongPasswordHashes() {
        String longHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

        userEntity.setPasswordHash(longHash);

        assertEquals(longHash, userEntity.getPasswordHash());
    }

    @Test
    @DisplayName("Should preserve UUID format")
    void shouldPreserveUuidFormat() {
        UUID specificId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

        userEntity.setId(specificId);

        assertEquals(specificId, userEntity.getId());
        assertEquals("550e8400-e29b-41d4-a716-446655440000", userEntity.getId().toString());
    }

}