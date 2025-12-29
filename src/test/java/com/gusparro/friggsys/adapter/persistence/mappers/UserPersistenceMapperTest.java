package com.gusparro.friggsys.adapter.persistence.mappers;

import com.gusparro.friggsys.adapter.persistence.entities.UserEntity;
import com.gusparro.friggsys.domain.entities.User;
import com.gusparro.friggsys.domain.enums.UserStatus;
import com.gusparro.friggsys.domain.vos.Email;
import com.gusparro.friggsys.domain.vos.Name;
import com.gusparro.friggsys.domain.vos.Password;
import com.gusparro.friggsys.domain.vos.Telephone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserPersistenceMapper Tests")
class UserPersistenceMapperTest {

    private UserPersistenceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserPersistenceMapper();
    }

    @Test
    @DisplayName("Should convert User domain to UserEntity correctly")
    void shouldConvertUserDomainToUserEntityCorrectly() {
        var id = UUID.randomUUID();
        var name = Name.of("John Doe");
        var email = Email.of("john@example.com");
        var telephone = Telephone.of("(11) 99999-9999");
        var password = Password.ofHash("hashedPassword123");
        var status = UserStatus.ACTIVE;
        var createdAt = OffsetDateTime.now();
        var updatedAt = OffsetDateTime.now();

        var user = User.reconstruct(id, name, email, telephone, password, status, createdAt, updatedAt);

        var entity = mapper.toEntity(user);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals(name.getValue(), entity.getName());
        assertEquals(email.getValue(), entity.getEmail());
        assertEquals(telephone.getValue(), entity.getTelephone());
        assertEquals(password.getValue(), entity.getPasswordHash());
        assertEquals(status, entity.getStatus());
        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(updatedAt, entity.getUpdatedAt());
    }

    @Test
    @DisplayName("Should convert UserEntity to User domain correctly")
    void shouldConvertUserEntityToUserDomainCorrectly() {
        var id = UUID.randomUUID();
        var name = "Jane Smith";
        var email = "jane@example.com";
        var telephone = "(11) 98888-8888";
        var passwordHash = "hashedPassword456";
        var status = UserStatus.ACTIVE;
        var createdAt = OffsetDateTime.now();
        var updatedAt = OffsetDateTime.now();

        var entity = new UserEntity(id, name, telephone, email, passwordHash, status, createdAt, updatedAt);

        var user = mapper.toDomain(entity);

        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(telephone, user.getTelephone());
        assertEquals(passwordHash, user.getPassword());
        assertEquals(status, user.getStatus());
        assertEquals(createdAt, user.getCreatedAt());
        assertEquals(updatedAt, user.getUpdatedAt());
    }

    @Test
    @DisplayName("Should map User with INACTIVE status correctly")
    void shouldMapUserWithInactiveStatusCorrectly() {
        var user = User.reconstruct(
                UUID.randomUUID(),
                Name.of("Inactive User"),
                Email.of("inactive@example.com"),
                Telephone.of("(11) 97777-7777"),
                Password.ofHash("hash123"),
                UserStatus.INACTIVE,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        var entity = mapper.toEntity(user);

        assertEquals(UserStatus.INACTIVE, entity.getStatus());
    }

    @Test
    @DisplayName("Should map UserEntity with INACTIVE status to domain correctly")
    void shouldMapUserEntityWithInactiveStatusToDomainCorrectly() {
        var entity = new UserEntity(
                UUID.randomUUID(),
                "User Name",
                "(11) 96666-6666",
                "user@example.com",
                "hashABC",
                UserStatus.INACTIVE,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        var user = mapper.toDomain(entity);

        assertEquals(UserStatus.INACTIVE, user.getStatus());
    }

    @Test
    @DisplayName("Should preserve UUID when mapping to entity")
    void shouldPreserveUuidWhenMappingToEntity() {
        var originalId = UUID.randomUUID();
        var user = User.reconstruct(
                originalId,
                Name.of("Test User"),
                Email.of("test@example.com"),
                Telephone.of("(11) 95555-5555"),
                Password.ofHash("hash"),
                UserStatus.ACTIVE,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        var entity = mapper.toEntity(user);

        assertEquals(originalId, entity.getId());
    }

    @Test
    @DisplayName("Should preserve UUID when mapping to domain")
    void shouldPreserveUuidWhenMappingToDomain() {
        var originalId = UUID.randomUUID();
        var entity = new UserEntity(
                originalId,
                "Mapped User",
                "(11) 94444-4444",
                "mapped@example.com",
                "hashXYZ",
                UserStatus.ACTIVE,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        var user = mapper.toDomain(entity);

        assertEquals(originalId, user.getId());
    }

    @Test
    @DisplayName("Should preserve timestamps when mapping to entity")
    void shouldPreserveTimestampsWhenMappingToEntity() {
        var createdAt = OffsetDateTime.parse("2025-01-01T10:00:00Z");
        var updatedAt = OffsetDateTime.parse("2025-01-15T15:30:00Z");

        var user = User.reconstruct(
                UUID.randomUUID(),
                Name.of("Timestamp Test"),
                Email.of("timestamp@example.com"),
                Telephone.of("(11) 93333-3333"),
                Password.ofHash("hash"),
                UserStatus.ACTIVE,
                createdAt,
                updatedAt
        );

        var entity = mapper.toEntity(user);

        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(updatedAt, entity.getUpdatedAt());
    }

    @Test
    @DisplayName("Should preserve timestamps when mapping to domain")
    void shouldPreserveTimestampsWhenMappingToDomain() {
        var createdAt = OffsetDateTime.parse("2024-06-10T08:00:00Z");
        var updatedAt = OffsetDateTime.parse("2024-12-20T12:45:00Z");

        var entity = new UserEntity(
                UUID.randomUUID(),
                "Time User",
                "(11) 92222-2222",
                "time@example.com",
                "hash",
                UserStatus.ACTIVE,
                createdAt,
                updatedAt
        );

        var user = mapper.toDomain(entity);

        assertEquals(createdAt, user.getCreatedAt());
        assertEquals(updatedAt, user.getUpdatedAt());
    }

    @Test
    @DisplayName("Should map complex name correctly to entity")
    void shouldMapComplexNameCorrectlyToEntity() {
        var complexName = Name.of("José Carlos da Silva Júnior");
        var user = User.reconstruct(
                UUID.randomUUID(),
                complexName,
                Email.of("jose@example.com"),
                Telephone.of("(11) 91111-1111"),
                Password.ofHash("hash"),
                UserStatus.ACTIVE,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        var entity = mapper.toEntity(user);

        assertEquals("José Carlos da Silva Júnior", entity.getName());
    }

    @Test
    @DisplayName("Should map complex name correctly to domain")
    void shouldMapComplexNameCorrectlyToDomain() {
        var entity = new UserEntity(
                UUID.randomUUID(),
                "María de los Ángeles García",
                "(11) 90000-0000",
                "maria@example.com",
                "hash",
                UserStatus.ACTIVE,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        var user = mapper.toDomain(entity);

        assertEquals("María de los Ángeles García", user.getName());
    }

    @Test
    @DisplayName("Should map different email formats correctly to entity")
    void shouldMapDifferentEmailFormatsCorrectlyToEntity() {
        var email1 = Email.of("simple@example.com");
        var email2 = Email.of("user.name+tag@example.co.uk");
        var email3 = Email.of("user123@subdomain.example.com");

        var user1 = User.reconstruct(UUID.randomUUID(), Name.of("User1"), email1,
                Telephone.of("(11) 99999-9991"), Password.ofHash("h1"), UserStatus.ACTIVE, OffsetDateTime.now(), OffsetDateTime.now());
        var user2 = User.reconstruct(UUID.randomUUID(), Name.of("User2"), email2,
                Telephone.of("(11) 99999-9992"), Password.ofHash("h2"), UserStatus.ACTIVE, OffsetDateTime.now(), OffsetDateTime.now());
        var user3 = User.reconstruct(UUID.randomUUID(), Name.of("User3"), email3,
                Telephone.of("(11) 99999-9993"), Password.ofHash("h3"), UserStatus.ACTIVE, OffsetDateTime.now(), OffsetDateTime.now());

        var entity1 = mapper.toEntity(user1);
        var entity2 = mapper.toEntity(user2);
        var entity3 = mapper.toEntity(user3);

        assertEquals("simple@example.com", entity1.getEmail());
        assertEquals("user.name+tag@example.co.uk", entity2.getEmail());
        assertEquals("user123@subdomain.example.com", entity3.getEmail());
    }

    @Test
    @DisplayName("Should map different email formats correctly to domain")
    void shouldMapDifferentEmailFormatsCorrectlyToDomain() {
        var entity1 = new UserEntity(UUID.randomUUID(), "User1", "(11) 99999-9991",
                "admin@example.com", "h1", UserStatus.ACTIVE, OffsetDateTime.now(), OffsetDateTime.now());
        var entity2 = new UserEntity(UUID.randomUUID(), "User2", "(11) 99999-9992",
                "contact.info@company.org", "h2", UserStatus.ACTIVE, OffsetDateTime.now(), OffsetDateTime.now());

        var user1 = mapper.toDomain(entity1);
        var user2 = mapper.toDomain(entity2);

        assertEquals("admin@example.com", user1.getEmail());
        assertEquals("contact.info@company.org", user2.getEmail());
    }

    @Test
    @DisplayName("Should map different telephone formats correctly to entity")
    void shouldMapDifferentTelephoneFormatsCorrectlyToEntity() {
        var tel1 = Telephone.of("(11) 99999-9999");
        var tel2 = Telephone.of("(21) 98888-8888");
        var tel3 = Telephone.of("(31) 97777-7777");

        var user1 = User.reconstruct(UUID.randomUUID(), Name.of("User1"), Email.of("u1@example.com"),
                tel1, Password.ofHash("h1"), UserStatus.ACTIVE, OffsetDateTime.now(), OffsetDateTime.now());
        var user2 = User.reconstruct(UUID.randomUUID(), Name.of("User2"), Email.of("u2@example.com"),
                tel2, Password.ofHash("h2"), UserStatus.ACTIVE, OffsetDateTime.now(), OffsetDateTime.now());
        var user3 = User.reconstruct(UUID.randomUUID(), Name.of("User3"), Email.of("u3@example.com"),
                tel3, Password.ofHash("h3"), UserStatus.ACTIVE, OffsetDateTime.now(), OffsetDateTime.now());

        var entity1 = mapper.toEntity(user1);
        var entity2 = mapper.toEntity(user2);
        var entity3 = mapper.toEntity(user3);

        assertEquals("(11) 99999-9999", entity1.getTelephone());
        assertEquals("(21) 98888-8888", entity2.getTelephone());
        assertEquals("(31) 97777-7777", entity3.getTelephone());
    }

    @Test
    @DisplayName("Should map different telephone formats correctly to domain")
    void shouldMapDifferentTelephoneFormatsCorrectlyToDomain() {
        var entity1 = new UserEntity(UUID.randomUUID(), "User1", "(41) 96666-6666",
                "u1@example.com", "h1", UserStatus.ACTIVE, OffsetDateTime.now(), OffsetDateTime.now());
        var entity2 = new UserEntity(UUID.randomUUID(), "User2", "(51) 95555-5555",
                "u2@example.com", "h2", UserStatus.ACTIVE, OffsetDateTime.now(), OffsetDateTime.now());

        var user1 = mapper.toDomain(entity1);
        var user2 = mapper.toDomain(entity2);

        assertEquals("(41) 96666-6666", user1.getTelephone());
        assertEquals("(51) 95555-5555", user2.getTelephone());
    }

    @Test
    @DisplayName("Should map password hash correctly to entity")
    void shouldMapPasswordHashCorrectlyToEntity() {
        var passwordHash = "bcrypt$2a$10$N9qo8uLOickgx2ZMRZoMye";
        var password = Password.ofHash(passwordHash);

        var user = User.reconstruct(
                UUID.randomUUID(),
                Name.of("User Test"),
                Email.of("user@example.com"),
                Telephone.of("(11) 94444-4444"),
                password,
                UserStatus.ACTIVE,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        var entity = mapper.toEntity(user);

        assertEquals(passwordHash, entity.getPasswordHash());
    }

    @Test
    @DisplayName("Should map password hash correctly to domain")
    void shouldMapPasswordHashCorrectlyToDomain() {
        var passwordHash = "argon2$v=19$m=4096,t=3,p=1$salt$hash";

        var entity = new UserEntity(
                UUID.randomUUID(),
                "User Test",
                "(11) 93333-3333",
                "user@example.com",
                passwordHash,
                UserStatus.ACTIVE,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        var user = mapper.toDomain(entity);

        assertEquals(passwordHash, user.getPassword());
    }

    @Test
    @DisplayName("Should handle bidirectional mapping correctly")
    void shouldHandleBidirectionalMappingCorrectly() {
        var originalUser = User.reconstruct(
                UUID.randomUUID(),
                Name.of("Bidirectional User"),
                Email.of("bidirectional@example.com"),
                Telephone.of("(11) 92222-2222"),
                Password.ofHash("originalHash"),
                UserStatus.ACTIVE,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        var entity = mapper.toEntity(originalUser);
        var mappedBackUser = mapper.toDomain(entity);

        assertEquals(originalUser.getId(), mappedBackUser.getId());
        assertEquals(originalUser.getName(), mappedBackUser.getName());
        assertEquals(originalUser.getEmail(), mappedBackUser.getEmail());
        assertEquals(originalUser.getTelephone(), mappedBackUser.getTelephone());
        assertEquals(originalUser.getPassword(), mappedBackUser.getPassword());
        assertEquals(originalUser.getStatus(), mappedBackUser.getStatus());
        assertEquals(originalUser.getCreatedAt(), mappedBackUser.getCreatedAt());
        assertEquals(originalUser.getUpdatedAt(), mappedBackUser.getUpdatedAt());
    }

    @Test
    @DisplayName("Should map multiple users to entities consistently")
    void shouldMapMultipleUsersToEntitiesConsistently() {
        var user1 = User.reconstruct(UUID.randomUUID(), Name.of("User One"), Email.of("one@example.com"),
                Telephone.of("(11) 91111-1111"), Password.ofHash("hash1"), UserStatus.ACTIVE, OffsetDateTime.now(), OffsetDateTime.now());
        var user2 = User.reconstruct(UUID.randomUUID(), Name.of("User Two"), Email.of("two@example.com"),
                Telephone.of("(11) 92222-2222"), Password.ofHash("hash2"), UserStatus.INACTIVE, OffsetDateTime.now(), OffsetDateTime.now());
        var user3 = User.reconstruct(UUID.randomUUID(), Name.of("User Three"), Email.of("three@example.com"),
                Telephone.of("(11) 93333-3333"), Password.ofHash("hash3"), UserStatus.ACTIVE, OffsetDateTime.now(), OffsetDateTime.now());

        var entity1 = mapper.toEntity(user1);
        var entity2 = mapper.toEntity(user2);
        var entity3 = mapper.toEntity(user3);

        assertEquals("User One", entity1.getName());
        assertEquals("User Two", entity2.getName());
        assertEquals("User Three", entity3.getName());
        assertEquals(UserStatus.ACTIVE, entity1.getStatus());
        assertEquals(UserStatus.INACTIVE, entity2.getStatus());
        assertEquals(UserStatus.ACTIVE, entity3.getStatus());
    }

    @Test
    @DisplayName("Should map multiple entities to users consistently")
    void shouldMapMultipleEntitiesToUsersConsistently() {
        var entity1 = new UserEntity(UUID.randomUUID(), "Entity One", "(11) 94444-4444",
                "entity1@example.com", "hash1", UserStatus.ACTIVE, OffsetDateTime.now(), OffsetDateTime.now());
        var entity2 = new UserEntity(UUID.randomUUID(), "Entity Two", "(11) 95555-5555",
                "entity2@example.com", "hash2", UserStatus.INACTIVE, OffsetDateTime.now(), OffsetDateTime.now());

        var user1 = mapper.toDomain(entity1);
        var user2 = mapper.toDomain(entity2);

        assertEquals("Entity One", user1.getName());
        assertEquals("Entity Two", user2.getName());
        assertEquals("entity1@example.com", user1.getEmail());
        assertEquals("entity2@example.com", user2.getEmail());
    }

    @Test
    @DisplayName("Should maintain data integrity when mapping with recent timestamps")
    void shouldMaintainDataIntegrityWhenMappingWithRecentTimestamps() {
        var now = OffsetDateTime.now();
        var user = User.reconstruct(
                UUID.randomUUID(),
                Name.of("Recent User"),
                Email.of("recent@example.com"),
                Telephone.of("(11) 96666-6666"),
                Password.ofHash("recentHash"),
                UserStatus.ACTIVE,
                now,
                now
        );

        var entity = mapper.toEntity(user);

        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
    }

    @Test
    @DisplayName("Should maintain data integrity when mapping with old timestamps")
    void shouldMaintainDataIntegrityWhenMappingWithOldTimestamps() {
        var oldDate = OffsetDateTime.parse("2020-01-01T00:00:00Z");
        var entity = new UserEntity(
                UUID.randomUUID(),
                "Old User",
                "(11) 97777-7777",
                "old@example.com",
                "oldHash",
                UserStatus.ACTIVE,
                oldDate,
                oldDate
        );

        var user = mapper.toDomain(entity);

        assertEquals(oldDate, user.getCreatedAt());
        assertEquals(oldDate, user.getUpdatedAt());
    }

    @Test
    @DisplayName("Should map all UserStatus values correctly to entity")
    void shouldMapAllUserStatusValuesCorrectlyToEntity() {
        var activeUser = User.reconstruct(UUID.randomUUID(), Name.of("Active"), Email.of("active@example.com"),
                Telephone.of("(11) 98888-8881"), Password.ofHash("h1"), UserStatus.ACTIVE, OffsetDateTime.now(), OffsetDateTime.now());
        var inactiveUser = User.reconstruct(UUID.randomUUID(), Name.of("Inactive"), Email.of("inactive@example.com"),
                Telephone.of("(11) 98888-8882"), Password.ofHash("h2"), UserStatus.INACTIVE, OffsetDateTime.now(), OffsetDateTime.now());

        var activeEntity = mapper.toEntity(activeUser);
        var inactiveEntity = mapper.toEntity(inactiveUser);

        assertEquals(UserStatus.ACTIVE, activeEntity.getStatus());
        assertEquals(UserStatus.INACTIVE, inactiveEntity.getStatus());
    }

    @Test
    @DisplayName("Should map all UserStatus values correctly to domain")
    void shouldMapAllUserStatusValuesCorrectlyToDomain() {
        var activeEntity = new UserEntity(UUID.randomUUID(), "Active", "(11) 98888-8881",
                "active@example.com", "h1", UserStatus.ACTIVE, OffsetDateTime.now(), OffsetDateTime.now());
        var inactiveEntity = new UserEntity(UUID.randomUUID(), "Inactive", "(11) 98888-8882",
                "inactive@example.com", "h2", UserStatus.INACTIVE, OffsetDateTime.now(), OffsetDateTime.now());

        var activeUser = mapper.toDomain(activeEntity);
        var inactiveUser = mapper.toDomain(inactiveEntity);

        assertEquals(UserStatus.ACTIVE, activeUser.getStatus());
        assertEquals(UserStatus.INACTIVE, inactiveUser.getStatus());
    }

    @Test
    @DisplayName("Should create new mapper instance successfully")
    void shouldCreateNewMapperInstanceSuccessfully() {
        var newMapper = new UserPersistenceMapper();

        assertNotNull(newMapper);
    }

    @Test
    @DisplayName("Should handle mapping with different UUIDs")
    void shouldHandleMappingWithDifferentUUIDs() {
        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();
        var id3 = UUID.randomUUID();

        var user1 = User.reconstruct(id1, Name.of("User1"), Email.of("u1@example.com"),
                Telephone.of("(11) 99999-9991"), Password.ofHash("h1"), UserStatus.ACTIVE, OffsetDateTime.now(), OffsetDateTime.now());
        var user2 = User.reconstruct(id2, Name.of("User2"), Email.of("u2@example.com"),
                Telephone.of("(11) 99999-9992"), Password.ofHash("h2"), UserStatus.ACTIVE, OffsetDateTime.now(), OffsetDateTime.now());
        var user3 = User.reconstruct(id3, Name.of("User3"), Email.of("u3@example.com"),
                Telephone.of("(11) 99999-9993"), Password.ofHash("h3"), UserStatus.ACTIVE, OffsetDateTime.now(), OffsetDateTime.now());

        var entity1 = mapper.toEntity(user1);
        var entity2 = mapper.toEntity(user2);
        var entity3 = mapper.toEntity(user3);

        assertEquals(id1, entity1.getId());
        assertEquals(id2, entity2.getId());
        assertEquals(id3, entity3.getId());
        assertNotEquals(entity1.getId(), entity2.getId());
        assertNotEquals(entity2.getId(), entity3.getId());
    }

    @Test
    @DisplayName("Should preserve all value objects when mapping to entity")
    void shouldPreserveAllValueObjectsWhenMappingToEntity() {
        var name = Name.of("Complete User");
        var email = Email.of("complete@example.com");
        var telephone = Telephone.of("(11) 90000-0000");
        var password = Password.ofHash("completeHash123");

        var user = User.reconstruct(
                UUID.randomUUID(),
                name,
                email,
                telephone,
                password,
                UserStatus.ACTIVE,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        var entity = mapper.toEntity(user);

        assertEquals(name.getValue(), entity.getName());
        assertEquals(email.getValue(), entity.getEmail());
        assertEquals(telephone.getValue(), entity.getTelephone());
        assertEquals(password.getValue(), entity.getPasswordHash());
    }

    @Test
    @DisplayName("Should correctly reconstruct value objects when mapping to domain")
    void shouldCorrectlyReconstructValueObjectsWhenMappingToDomain() {
        var entity = new UserEntity(
                UUID.randomUUID(),
                "Reconstruct User",
                "(11) 91111-1111",
                "reconstruct@example.com",
                "reconstructHash456",
                UserStatus.ACTIVE,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        var user = mapper.toDomain(entity);

        assertNotNull(user.getName());
        assertNotNull(user.getEmail());
        assertNotNull(user.getTelephone());
        assertNotNull(user.getPassword());
        assertEquals("Reconstruct User", user.getName());
        assertEquals("reconstruct@example.com", user.getEmail());
        assertEquals("(11) 91111-1111", user.getTelephone());
        assertEquals("reconstructHash456", user.getPassword());
    }
    
}