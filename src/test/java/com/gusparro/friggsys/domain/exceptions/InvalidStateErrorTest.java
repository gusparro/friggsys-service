package com.gusparro.friggsys.domain.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InvalidStateError Tests")
class InvalidStateErrorTest {

    @Test
    @DisplayName("Should create invalid state error with basic parameters")
    void shouldCreateInvalidStateErrorWithBasicParameters() {
        var error = new InvalidStateError("User", "inactive", "delete");

        assertEquals("It is not possible to execute 'delete' on User in the 'inactive' state", error.getMessage());
        assertEquals("User", error.getEntityName());
        assertEquals("inactive", error.getCurrentState());
        assertEquals("delete", error.getAction());
        assertNotNull(error.getDetails());
        assertTrue(error.getDetails().isEmpty());
    }

    @Test
    @DisplayName("Should create invalid state error with details")
    void shouldCreateInvalidStateErrorWithDetails() {
        Map<String, Object> details = Map.of("userId", "123", "attemptedBy", "admin");

        var error = new InvalidStateError("Order", "shipped", "cancel", details);

        assertEquals("It is not possible to execute 'cancel' on Order in the 'shipped' state", error.getMessage());
        assertEquals("Order", error.getEntityName());
        assertEquals("shipped", error.getCurrentState());
        assertEquals("cancel", error.getAction());
        assertEquals(2, error.getDetails().size());
        assertEquals("123", error.getDetails().get("userId"));
        assertEquals("admin", error.getDetails().get("attemptedBy"));
    }

    @Test
    @DisplayName("Should format message correctly with different parameters")
    void shouldFormatMessageCorrectlyWithDifferentParameters() {
        var error = new InvalidStateError("Account", "suspended", "withdraw");

        assertEquals("It is not possible to execute 'withdraw' on Account in the 'suspended' state",
                error.getMessage());
    }
}