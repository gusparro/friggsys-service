package com.gusparro.friggsys.usecase.user.services;

import com.gusparro.friggsys.domain.vos.Password;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PasswordEncoderService Tests")
class PasswordEncoderServiceTest {

    @Mock
    private PasswordEncoderService passwordEncoderService;

    private Password rawPassword;
    private Password encryptedPassword;

    private static final String RAW_PASSWORD_VALUE = "MySecureP@ssw0rd";
    private static final String ENCRYPTED_PASSWORD_VALUE = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

    @BeforeEach
    void setUp() {
        rawPassword = Password.ofRaw(RAW_PASSWORD_VALUE);
        encryptedPassword = Password.ofHash(ENCRYPTED_PASSWORD_VALUE);
    }

    @Test
    @DisplayName("Should encrypt raw password successfully")
    void shouldEncryptRawPasswordSuccessfully() {
        when(passwordEncoderService.encrypt(any(Password.class))).thenReturn(encryptedPassword);

        var result = passwordEncoderService.encrypt(rawPassword);

        assertNotNull(result);
        assertNotEquals(rawPassword.getValue(), result.getValue());
        assertEquals(ENCRYPTED_PASSWORD_VALUE, result.getValue());
        verify(passwordEncoderService, times(1)).encrypt(rawPassword);
    }

    @Test
    @DisplayName("Should return different encrypted values for same raw password")
    void shouldReturnDifferentEncryptedValuesForSameRawPassword() {
        Password firstEncrypted = Password.ofRaw("$2a$10$firstHash");
        Password secondEncrypted = Password.ofRaw("$2a$10$secondHash");

        when(passwordEncoderService.encrypt(any(Password.class)))
                .thenReturn(firstEncrypted)
                .thenReturn(secondEncrypted);

        var result1 = passwordEncoderService.encrypt(rawPassword);
        var result2 = passwordEncoderService.encrypt(rawPassword);

        assertNotNull(result1);
        assertNotNull(result2);
        assertNotEquals(result1.getValue(), result2.getValue());
        verify(passwordEncoderService, times(2)).encrypt(rawPassword);
    }

    @Test
    @DisplayName("Should match raw password with its encrypted version")
    void shouldMatchRawPasswordWithEncryptedVersion() {
        when(passwordEncoderService.matches(anyString(), anyString())).thenReturn(true);

        var matches = passwordEncoderService.matches(RAW_PASSWORD_VALUE, ENCRYPTED_PASSWORD_VALUE);

        assertTrue(matches);
        verify(passwordEncoderService, times(1))
                .matches(RAW_PASSWORD_VALUE, ENCRYPTED_PASSWORD_VALUE);
    }

    @Test
    @DisplayName("Should not match raw password with different encrypted password")
    void shouldNotMatchRawPasswordWithDifferentEncryptedPassword() {
        var wrongPassword = "WrongP@ssw0rd";

        when(passwordEncoderService.matches(eq(wrongPassword), anyString())).thenReturn(false);

        var matches = passwordEncoderService.matches(wrongPassword, ENCRYPTED_PASSWORD_VALUE);

        assertFalse(matches);
        verify(passwordEncoderService, times(1))
                .matches(wrongPassword, ENCRYPTED_PASSWORD_VALUE);
    }

    @Test
    @DisplayName("Should handle null raw password in encrypt")
    void shouldHandleNullRawPasswordInEncrypt() {
        when(passwordEncoderService.encrypt(null))
                .thenThrow(new IllegalArgumentException("Password cannot be null"));

        assertThrows(IllegalArgumentException.class,
                () -> passwordEncoderService.encrypt(null));
        verify(passwordEncoderService, times(1)).encrypt(null);
    }

    @Test
    @DisplayName("Should handle null parameters in matches")
    void shouldHandleNullParametersInMatches() {
        when(passwordEncoderService.matches(isNull(), eq(ENCRYPTED_PASSWORD_VALUE))).thenReturn(false);
        when(passwordEncoderService.matches(eq(RAW_PASSWORD_VALUE), isNull())).thenReturn(false);

        var matchesNullRaw = passwordEncoderService.matches(null, ENCRYPTED_PASSWORD_VALUE);
        var matchesNullEncrypted = passwordEncoderService.matches(RAW_PASSWORD_VALUE, null);

        assertFalse(matchesNullRaw);
        assertFalse(matchesNullEncrypted);
        verify(passwordEncoderService, times(1)).matches(isNull(), eq(ENCRYPTED_PASSWORD_VALUE));
        verify(passwordEncoderService, times(1)).matches(eq(RAW_PASSWORD_VALUE), isNull());
    }

    @Test
    @DisplayName("Should handle empty string in matches")
    void shouldHandleEmptyStringInMatches() {
        when(passwordEncoderService.matches(eq(""), anyString())).thenReturn(false);

        var matches = passwordEncoderService.matches("", ENCRYPTED_PASSWORD_VALUE);

        assertFalse(matches);
        verify(passwordEncoderService, times(1)).matches("", ENCRYPTED_PASSWORD_VALUE);
    }

    @Test
    @DisplayName("Should verify encrypt method is called with correct password")
    void shouldVerifyEncryptMethodCalledWithCorrectPassword() {
        when(passwordEncoderService.encrypt(any(Password.class))).thenReturn(encryptedPassword);

        passwordEncoderService.encrypt(rawPassword);

        verify(passwordEncoderService).encrypt(argThat(password ->
                password != null && password.getValue().equals(RAW_PASSWORD_VALUE)
        ));
    }
}
