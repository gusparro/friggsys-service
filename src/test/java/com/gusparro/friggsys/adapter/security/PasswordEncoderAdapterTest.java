package com.gusparro.friggsys.adapter.security;

import com.gusparro.friggsys.domain.vos.Password;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PasswordEncoderAdapter Tests")
class PasswordEncoderAdapterTest {

    private PasswordEncoderAdapter passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new PasswordEncoderAdapter();
    }

    @Test
    @DisplayName("Should encrypt password and return non-null Password object")
    void shouldEncryptPasswordAndReturnNonNullPasswordObject() {
        var rawPassword = Password.ofRaw("SecurePass123!");

        var encryptedPassword = passwordEncoder.encrypt(rawPassword);

        assertNotNull(encryptedPassword);
        assertNotNull(encryptedPassword.getValue());
    }

    @Test
    @DisplayName("Should encrypt password and return different value from raw password")
    void shouldEncryptPasswordAndReturnDifferentValueFromRawPassword() {
        var rawPassword = Password.ofRaw("PlainText123!");

        var encryptedPassword = passwordEncoder.encrypt(rawPassword);

        assertNotEquals(rawPassword.getValue(), encryptedPassword.getValue());
    }

    @Test
    @DisplayName("Should encrypt password and generate BCrypt hash")
    void shouldEncryptPasswordAndGenerateBCryptHash() {
        var rawPassword = Password.ofRaw("TestPass123!");

        var encryptedPassword = passwordEncoder.encrypt(rawPassword);

        assertTrue(encryptedPassword.getValue().startsWith("$2a$") ||
                encryptedPassword.getValue().startsWith("$2b$") ||
                encryptedPassword.getValue().startsWith("$2y$"));
    }

    @Test
    @DisplayName("Should encrypt same password twice and generate different hashes")
    void shouldEncryptSamePasswordTwiceAndGenerateDifferentHashes() {
        var rawPassword = Password.ofRaw("SamePass123!");

        var encrypted1 = passwordEncoder.encrypt(rawPassword);
        var encrypted2 = passwordEncoder.encrypt(rawPassword);

        assertNotEquals(encrypted1.getValue(), encrypted2.getValue());
    }

    @Test
    @DisplayName("Should encrypt password with minimum valid requirements")
    void shouldEncryptPasswordWithMinimumValidRequirements() {
        var rawPassword = Password.ofRaw("Pass123!"); // 8 chars

        var encryptedPassword = passwordEncoder.encrypt(rawPassword);

        assertNotNull(encryptedPassword);
        assertNotNull(encryptedPassword.getValue());
        assertTrue(encryptedPassword.getValue().length() > rawPassword.getValue().length());
    }

    @Test
    @DisplayName("Should encrypt password with maximum length")
    void shouldEncryptPasswordWithMaximumLength() {
        var longPassword = "Aa1!" + "a".repeat(46); // 50 chars total
        var rawPassword = Password.ofRaw(longPassword);

        var encryptedPassword = passwordEncoder.encrypt(rawPassword);

        assertNotNull(encryptedPassword);
        assertNotNull(encryptedPassword.getValue());
    }

    @Test
    @DisplayName("Should encrypt password with all required special characters")
    void shouldEncryptPasswordWithAllRequiredSpecialCharacters() {
        var rawPassword = Password.ofRaw("P@ssw0rd!#$%");

        var encryptedPassword = passwordEncoder.encrypt(rawPassword);

        assertNotNull(encryptedPassword);
        assertNotEquals(rawPassword.getValue(), encryptedPassword.getValue());
    }

    @Test
    @DisplayName("Should encrypt password with complex combination")
    void shouldEncryptPasswordWithComplexCombination() {
        var rawPassword = Password.ofRaw("C0mpl3x!P@ssw0rd");

        var encryptedPassword = passwordEncoder.encrypt(rawPassword);

        assertNotNull(encryptedPassword);
        assertNotNull(encryptedPassword.getValue());
    }

    @Test
    @DisplayName("Should match raw password with encrypted password")
    void shouldMatchRawPasswordWithEncryptedPassword() {
        var rawPassword = Password.ofRaw("Correct123!");
        var encryptedPassword = passwordEncoder.encrypt(rawPassword);

        var matches = passwordEncoder.matches(rawPassword.getValue(), encryptedPassword.getValue());

        assertTrue(matches);
    }

    @Test
    @DisplayName("Should not match different raw password with encrypted password")
    void shouldNotMatchDifferentRawPasswordWithEncryptedPassword() {
        var rawPassword = Password.ofRaw("Correct123!");
        var wrongPassword = "Wrong456!";
        var encryptedPassword = passwordEncoder.encrypt(rawPassword);

        var matches = passwordEncoder.matches(wrongPassword, encryptedPassword.getValue());

        assertFalse(matches);
    }

    @Test
    @DisplayName("Should not match empty password with encrypted password")
    void shouldNotMatchEmptyPasswordWithEncryptedPassword() {
        var rawPassword = Password.ofRaw("SomePass123!");
        var encryptedPassword = passwordEncoder.encrypt(rawPassword);

        var matches = passwordEncoder.matches("", encryptedPassword.getValue());

        assertFalse(matches);
    }

    @Test
    @DisplayName("Should match password with case sensitivity")
    void shouldMatchPasswordWithCaseSensitivity() {
        var rawPassword = Password.ofRaw("Password123!");
        var encryptedPassword = passwordEncoder.encrypt(rawPassword);

        var matchesCorrect = passwordEncoder.matches("Password123!", encryptedPassword.getValue());
        var matchesWrongCase = passwordEncoder.matches("password123!", encryptedPassword.getValue());

        assertTrue(matchesCorrect);
        assertFalse(matchesWrongCase);
    }

    @Test
    @DisplayName("Should match password with special characters correctly")
    void shouldMatchPasswordWithSpecialCharactersCorrectly() {
        var rawPassword = Password.ofRaw("P@ssw0rd!#$");
        var encryptedPassword = passwordEncoder.encrypt(rawPassword);

        var matches = passwordEncoder.matches("P@ssw0rd!#$", encryptedPassword.getValue());

        assertTrue(matches);
    }

    @Test
    @DisplayName("Should not match password with slight difference")
    void shouldNotMatchPasswordWithSlightDifference() {
        var rawPassword = Password.ofRaw("Password123!");
        var encryptedPassword = passwordEncoder.encrypt(rawPassword);

        var matchesWithExtraChar = passwordEncoder.matches("Password1234!", encryptedPassword.getValue());
        var matchesWithMissingChar = passwordEncoder.matches("Password12!", encryptedPassword.getValue());

        assertFalse(matchesWithExtraChar);
        assertFalse(matchesWithMissingChar);
    }

    @Test
    @DisplayName("Should encrypt multiple different passwords correctly")
    void shouldEncryptMultipleDifferentPasswordsCorrectly() {
        var password1 = Password.ofRaw("FirstPass123!");
        var password2 = Password.ofRaw("SecondPass456!");
        var password3 = Password.ofRaw("ThirdPass789!");

        var encrypted1 = passwordEncoder.encrypt(password1);
        var encrypted2 = passwordEncoder.encrypt(password2);
        var encrypted3 = passwordEncoder.encrypt(password3);

        assertNotEquals(encrypted1.getValue(), encrypted2.getValue());
        assertNotEquals(encrypted2.getValue(), encrypted3.getValue());
        assertNotEquals(encrypted1.getValue(), encrypted3.getValue());
    }

    @Test
    @DisplayName("Should match each password with its own encrypted version")
    void shouldMatchEachPasswordWithItsOwnEncryptedVersion() {
        var password1 = Password.ofRaw("Password1!");
        var password2 = Password.ofRaw("Password2!");
        var password3 = Password.ofRaw("Password3!");

        var encrypted1 = passwordEncoder.encrypt(password1);
        var encrypted2 = passwordEncoder.encrypt(password2);
        var encrypted3 = passwordEncoder.encrypt(password3);

        assertTrue(passwordEncoder.matches(password1.getValue(), encrypted1.getValue()));
        assertTrue(passwordEncoder.matches(password2.getValue(), encrypted2.getValue()));
        assertTrue(passwordEncoder.matches(password3.getValue(), encrypted3.getValue()));

        assertFalse(passwordEncoder.matches(password1.getValue(), encrypted2.getValue()));
        assertFalse(passwordEncoder.matches(password2.getValue(), encrypted3.getValue()));
        assertFalse(passwordEncoder.matches(password3.getValue(), encrypted1.getValue()));
    }

    @Test
    @DisplayName("Should handle passwords with all character types")
    void shouldHandlePasswordsWithAllCharacterTypes() {
        var complexPassword = Password.ofRaw("Abc123!@#");

        var encrypted = passwordEncoder.encrypt(complexPassword);

        assertNotNull(encrypted);
        assertTrue(passwordEncoder.matches("Abc123!@#", encrypted.getValue()));
        assertFalse(passwordEncoder.matches("Abc123!@", encrypted.getValue()));
    }

    @Test
    @DisplayName("Should handle passwords with numbers at different positions")
    void shouldHandlePasswordsWithNumbersAtDifferentPositions() {
        var password1 = Password.ofRaw("1Password!");
        var password2 = Password.ofRaw("Pass1word!");
        var password3 = Password.ofRaw("Password1!");

        var encrypted1 = passwordEncoder.encrypt(password1);
        var encrypted2 = passwordEncoder.encrypt(password2);
        var encrypted3 = passwordEncoder.encrypt(password3);

        assertTrue(passwordEncoder.matches("1Password!", encrypted1.getValue()));
        assertTrue(passwordEncoder.matches("Pass1word!", encrypted2.getValue()));
        assertTrue(passwordEncoder.matches("Password1!", encrypted3.getValue()));
    }

    @Test
    @DisplayName("Should generate hash with fixed length")
    void shouldGenerateHashWithFixedLength() {
        var password1 = Password.ofRaw("Short1!A");
        var password2 = Password.ofRaw("MuchLongerPassword123!WithManyChars");

        var encrypted1 = passwordEncoder.encrypt(password1);
        var encrypted2 = passwordEncoder.encrypt(password2);

        assertEquals(60, encrypted1.getValue().length());
        assertEquals(60, encrypted2.getValue().length());
    }

    @Test
    @DisplayName("Should encrypt password consistently across multiple instances")
    void shouldEncryptPasswordConsistentlyAcrossMultipleInstances() {
        var rawPassword = Password.ofRaw("TestPass123!");

        var encoder1 = new PasswordEncoderAdapter();
        var encoder2 = new PasswordEncoderAdapter();

        var encrypted = encoder1.encrypt(rawPassword);

        assertTrue(encoder2.matches(rawPassword.getValue(), encrypted.getValue()));
    }

    @Test
    @DisplayName("Should handle typical user registration scenario")
    void shouldHandleTypicalUserRegistrationScenario() {
        var userPassword = Password.ofRaw("MyP@ssw0rd123");

        var encryptedPassword = passwordEncoder.encrypt(userPassword);

        assertNotNull(encryptedPassword);
        assertNotEquals(userPassword.getValue(), encryptedPassword.getValue());
        assertTrue(passwordEncoder.matches(userPassword.getValue(), encryptedPassword.getValue()));
    }

    @Test
    @DisplayName("Should handle typical login verification scenario")
    void shouldHandleTypicalLoginVerificationScenario() {
        var registeredPassword = Password.ofRaw("UserPass123!");
        var storedHash = passwordEncoder.encrypt(registeredPassword);

        var correctLoginAttempt = "UserPass123!";
        var incorrectLoginAttempt = "UserPass124!";

        assertTrue(passwordEncoder.matches(correctLoginAttempt, storedHash.getValue()));
        assertFalse(passwordEncoder.matches(incorrectLoginAttempt, storedHash.getValue()));
    }

    @Test
    @DisplayName("Should handle password change scenario")
    void shouldHandlePasswordChangeScenario() {
        var oldPassword = Password.ofRaw("OldPass123!");
        var newPassword = Password.ofRaw("NewPass456!");

        var oldHash = passwordEncoder.encrypt(oldPassword);
        var newHash = passwordEncoder.encrypt(newPassword);

        assertNotEquals(oldHash.getValue(), newHash.getValue());
        assertTrue(passwordEncoder.matches(oldPassword.getValue(), oldHash.getValue()));
        assertTrue(passwordEncoder.matches(newPassword.getValue(), newHash.getValue()));
        assertFalse(passwordEncoder.matches(oldPassword.getValue(), newHash.getValue()));
        assertFalse(passwordEncoder.matches(newPassword.getValue(), oldHash.getValue()));
    }

    @Test
    @DisplayName("Should not match raw password that looks like BCrypt hash")
    void shouldNotMatchRawPasswordThatLooksLikeBCryptHash() {
        var realPassword = Password.ofRaw("RealPass123!");
        var fakeHashPassword = "$2a$10$abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOP";

        var encrypted = passwordEncoder.encrypt(realPassword);

        assertFalse(passwordEncoder.matches(fakeHashPassword, encrypted.getValue()));
    }

    @Test
    @DisplayName("Should handle passwords with multiple special characters")
    void shouldHandlePasswordsWithMultipleSpecialCharacters() {
        var passwordWithSpaces = Password.ofRaw("Pass Word123!");
        var passwordWithSymbols = Password.ofRaw("P@ss!W#rd$123");

        var encrypted1 = passwordEncoder.encrypt(passwordWithSpaces);
        var encrypted2 = passwordEncoder.encrypt(passwordWithSymbols);

        assertTrue(passwordEncoder.matches("Pass Word123!", encrypted1.getValue()));
        assertTrue(passwordEncoder.matches("P@ss!W#rd$123", encrypted2.getValue()));
        assertFalse(passwordEncoder.matches("PassWord123!", encrypted1.getValue()));
    }

    @Test
    @DisplayName("Should return Password object of hash type")
    void shouldReturnPasswordObjectOfHashType() {
        var rawPassword = Password.ofRaw("TestPass123!");

        var encryptedPassword = passwordEncoder.encrypt(rawPassword);

        assertNotNull(encryptedPassword);
        assertInstanceOf(Password.class, encryptedPassword);
    }

    @Test
    @DisplayName("Should handle password with minimum of each required character type")
    void shouldHandlePasswordWithMinimumOfEachRequiredCharacterType() {
        var minimalPassword = Password.ofRaw("Aa1!bcde"); // 1 upper, 1 lower, 1 digit, 1 special, 8 total

        var encrypted = passwordEncoder.encrypt(minimalPassword);

        assertNotNull(encrypted);
        assertTrue(passwordEncoder.matches("Aa1!bcde", encrypted.getValue()));
    }

    @Test
    @DisplayName("Should encrypt different valid password formats")
    void shouldEncryptDifferentValidPasswordFormats() {
        var password1 = Password.ofRaw("Simple123!");
        var password2 = Password.ofRaw("C0mpl3x!P@ssw0rd#2024");
        var password3 = Password.ofRaw("Test!123Abc");

        var encrypted1 = passwordEncoder.encrypt(password1);
        var encrypted2 = passwordEncoder.encrypt(password2);
        var encrypted3 = passwordEncoder.encrypt(password3);

        assertTrue(passwordEncoder.matches("Simple123!", encrypted1.getValue()));
        assertTrue(passwordEncoder.matches("C0mpl3x!P@ssw0rd#2024", encrypted2.getValue()));
        assertTrue(passwordEncoder.matches("Test!123Abc", encrypted3.getValue()));
    }

    @Test
    @DisplayName("Should handle hash created from password object")
    void shouldHandleHashCreatedFromPasswordObject() {
        var rawPassword = Password.ofRaw("SecurePass123!");
        var encryptedPassword = passwordEncoder.encrypt(rawPassword);

        var hashPassword = Password.ofHash(encryptedPassword.getValue());

        assertNotNull(hashPassword);
        assertEquals(encryptedPassword.getValue(), hashPassword.getValue());
    }

}