package com.gusparro.friggsys.usecase.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EntityNotFoundError Tests")
class EntityNotFoundErrorTest {

    @Test
    @DisplayName("Should create exception with all parameters and details")
    void shouldCreateExceptionWithAllParametersAndDetails() {
        Map<String, Object> details = Map.of("operation", "find", "timestamp", "2024-01-01");

        var error = new EntityNotFoundError("User", "ID", "123", details);

        assertEquals("User with 'ID' '123' not found", error.getMessage());
        assertEquals("User", error.getEntityName());
        assertEquals("ID", error.getIdentifierType());
        assertEquals("123", error.getIdentifier());
        assertEquals(2, error.getDetails().size());
        assertEquals("find", error.getDetails().get("operation"));
    }

    @Test
    @DisplayName("Should create exception with null details")
    void shouldCreateExceptionWithNullDetails() {
        var error = new EntityNotFoundError("Order", "OrderNumber", "ORD-456", null);

        assertEquals("Order with 'OrderNumber' 'ORD-456' not found", error.getMessage());
        assertEquals("Order", error.getEntityName());
        assertEquals("OrderNumber", error.getIdentifierType());
        assertEquals("ORD-456", error.getIdentifier());
        assertNotNull(error.getDetails());
        assertTrue(error.getDetails().isEmpty());
    }

    @Test
    @DisplayName("Should create exception with empty details")
    void shouldCreateExceptionWithEmptyDetails() {
        var error = new EntityNotFoundError("Product", "SKU", "PROD-789", new HashMap<>());

        assertEquals("Product with 'SKU' 'PROD-789' not found", error.getMessage());
        assertEquals("Product", error.getEntityName());
        assertEquals("SKU", error.getIdentifierType());
        assertEquals("PROD-789", error.getIdentifier());
        assertTrue(error.getDetails().isEmpty());
    }

    @Test
    @DisplayName("Should format message correctly with UUID identifier")
    void shouldFormatMessageCorrectlyWithUuidIdentifier() {
        UUID uuid = UUID.randomUUID();
        var error = new EntityNotFoundError("Customer", "UUID", uuid.toString(), null);

        assertEquals(String.format("Customer with 'UUID' '%s' not found", uuid), error.getMessage());
        assertEquals("Customer", error.getEntityName());
        assertEquals("UUID", error.getIdentifierType());
        assertEquals(uuid.toString(), error.getIdentifier());
    }

    @Test
    @DisplayName("Should format message correctly with email identifier")
    void shouldFormatMessageCorrectlyWithEmailIdentifier() {
        String email = "user@example.com";
        var error = new EntityNotFoundError("Account", "Email", email, null);

        assertEquals("Account with 'Email' 'user@example.com' not found", error.getMessage());
        assertEquals("Account", error.getEntityName());
        assertEquals("Email", error.getIdentifierType());
        assertEquals(email, error.getIdentifier());
    }

    @Test
    @DisplayName("Should extend UseCaseException")
    void shouldExtendUseCaseException() {
        var error = new EntityNotFoundError("Entity", "ID", "1", null);

        assertInstanceOf(UseCaseException.class, error);
        assertInstanceOf(RuntimeException.class, error);
    }

    @Test
    @DisplayName("Should be throwable")
    void shouldBeThrowable() {
        assertThrows(EntityNotFoundError.class, () -> {
            throw new EntityNotFoundError("User", "ID", "999", null);
        });
    }

    @Test
    @DisplayName("Should allow adding details after creation")
    void shouldAllowAddingDetailsAfterCreation() {
        var error = new EntityNotFoundError("Invoice", "InvoiceId", "INV-001", new HashMap<>());

        error.addDetail("searchedAt", "2024-01-15T10:30:00");
        error.addDetail("requestedBy", "admin");

        assertEquals(2, error.getDetails().size());
        assertEquals("2024-01-15T10:30:00", error.getDetails().get("searchedAt"));
        assertEquals("admin", error.getDetails().get("requestedBy"));
    }

    @Test
    @DisplayName("Should have no cause by default")
    void shouldHaveNoCauseByDefault() {
        var error = new EntityNotFoundError("Entity", "ID", "1", null);

        assertNull(error.getCause());
    }

    @Test
    @DisplayName("Should store entity metadata correctly")
    void shouldStoreEntityMetadataCorrectly() {
        var error = new EntityNotFoundError("Payment", "TransactionId", "TXN-999", null);

        assertEquals("Payment", error.getEntityName());
        assertEquals("TransactionId", error.getIdentifierType());
        assertEquals("TXN-999", error.getIdentifier());
    }

    @Test
    @DisplayName("Should handle different entity names")
    void shouldHandleDifferentEntityNames() {
        var error1 = new EntityNotFoundError("User", "ID", "1", null);
        var error2 = new EntityNotFoundError("Order", "ID", "2", null);
        var error3 = new EntityNotFoundError("Product", "ID", "3", null);

        assertEquals("User", error1.getEntityName());
        assertEquals("Order", error2.getEntityName());
        assertEquals("Product", error3.getEntityName());
    }

    @Test
    @DisplayName("Should handle different identifier types")
    void shouldHandleDifferentIdentifierTypes() {
        var error1 = new EntityNotFoundError("User", "ID", "123", null);
        var error2 = new EntityNotFoundError("User", "Email", "user@test.com", null);
        var error3 = new EntityNotFoundError("User", "Username", "johndoe", null);

        assertEquals("ID", error1.getIdentifierType());
        assertEquals("Email", error2.getIdentifierType());
        assertEquals("Username", error3.getIdentifierType());
    }

    @Test
    @DisplayName("Should create exception with complex details")
    void shouldCreateExceptionWithComplexDetails() {
        Map<String, Object> details = new HashMap<>();
        details.put("searchCriteria", Map.of("status", "active", "role", "admin"));
        details.put("attemptCount", 3);
        details.put("cacheChecked", true);

        var error = new EntityNotFoundError("User", "ID", "456", details);

        assertEquals(3, error.getDetails().size());
        assertInstanceOf(Map.class, error.getDetails().get("searchCriteria"));
        assertEquals(3, error.getDetails().get("attemptCount"));
        assertEquals(true, error.getDetails().get("cacheChecked"));
    }

    @Test
    @DisplayName("Should allow catching as UseCaseException")
    void shouldAllowCatchingAsUseCaseException() {
        try {
            throw new EntityNotFoundError("Document", "DocumentId", "DOC-001", null);
        } catch (UseCaseException e) {
            assertInstanceOf(EntityNotFoundError.class, e);
            assertEquals("Document with 'DocumentId' 'DOC-001' not found", e.getMessage());
        }
    }

    @Test
    @DisplayName("Should allow catching as RuntimeException")
    void shouldAllowCatchingAsRuntimeException() {
        try {
            throw new EntityNotFoundError("Session", "SessionToken", "TOKEN-XYZ", null);
        } catch (RuntimeException e) {
            assertInstanceOf(EntityNotFoundError.class, e);
            assertEquals("Session with 'SessionToken' 'TOKEN-XYZ' not found", e.getMessage());
        }
    }

    @Test
    @DisplayName("Should create exception for typical use case scenario")
    void shouldCreateExceptionForTypicalUseCaseScenario() {
        var userId = UUID.randomUUID();
        Map<String, Object> details = Map.of(
                "operation", "findById",
                "repository", "UserRepository",
                "timestamp", System.currentTimeMillis()
        );

        var error = new EntityNotFoundError("User", "ID", userId.toString(), details);

        assertEquals(String.format("User with 'ID' '%s' not found", userId), error.getMessage());
        assertEquals("User", error.getEntityName());
        assertEquals("ID", error.getIdentifierType());
        assertEquals(userId.toString(), error.getIdentifier());
        assertEquals(3, error.getDetails().size());
        assertEquals("findById", error.getDetails().get("operation"));
        assertEquals("UserRepository", error.getDetails().get("repository"));
    }

    @Test
    @DisplayName("Should handle numeric identifiers as strings")
    void shouldHandleNumericIdentifiersAsStrings() {
        var error = new EntityNotFoundError("Record", "ID", "12345", null);

        assertEquals("Record with 'ID' '12345' not found", error.getMessage());
        assertEquals("12345", error.getIdentifier());
        assertInstanceOf(String.class, error.getIdentifier());
    }

    @Test
    @DisplayName("Should handle special characters in identifiers")
    void shouldHandleSpecialCharactersInIdentifiers() {
        var error = new EntityNotFoundError("File", "Path", "/home/user/documents/file.txt", null);

        assertEquals("File with 'Path' '/home/user/documents/file.txt' not found", error.getMessage());
        assertEquals("/home/user/documents/file.txt", error.getIdentifier());
    }

    @Test
    @DisplayName("Should maintain immutability of entity metadata")
    void shouldMaintainImmutabilityOfEntityMetadata() {
        var error = new EntityNotFoundError("Category", "Slug", "electronics", null);

        assertEquals("Category", error.getEntityName());
        assertEquals("Slug", error.getIdentifierType());
        assertEquals("electronics", error.getIdentifier());

        assertEquals("Category", error.getEntityName());
        assertEquals("Slug", error.getIdentifierType());
        assertEquals("electronics", error.getIdentifier());
    }

}