package com.gusparro.friggsys.usecase.user;

import com.gusparro.friggsys.domain.entities.User;
import com.gusparro.friggsys.domain.exceptions.ValidationError;
import com.gusparro.friggsys.domain.repositories.UserRepositoryInterface;
import com.gusparro.friggsys.domain.vos.Email;
import com.gusparro.friggsys.domain.vos.Password;
import com.gusparro.friggsys.usecase.exceptions.DuplicateEmailError;
import com.gusparro.friggsys.usecase.user.dtos.CreateUserInput;
import com.gusparro.friggsys.usecase.user.services.PasswordEncoderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateUserUseCase Tests")
class CreateUserUseCaseTest {

    @Mock
    private UserRepositoryInterface repository;

    @Mock
    private PasswordEncoderService encoder;

    @InjectMocks
    private CreateUserUseCase useCase;

    @Mock
    private User savedUser;

    private CreateUserInput input;
    private static final String USER_NAME = "John Doe";
    private static final String USER_EMAIL = "john.doe@example.com";
    private static final String USER_TELEPHONE = "(63) 99999-9999";
    private static final String RAW_PASSWORD = "SecureP@ss123";
    private static final String ENCRYPTED_PASSWORD = "$2a$10$encrypted_password";

    @BeforeEach
    void setUp() {
        input = new CreateUserInput(USER_NAME, USER_EMAIL, USER_TELEPHONE, RAW_PASSWORD);
    }

    @Test
    @DisplayName("Should create user successfully when email does not exist")
    void shouldCreateUserSuccessfullyWhenEmailDoesNotExist() {
        var encryptedPassword = Password.ofHash(ENCRYPTED_PASSWORD);

        when(repository.existsByEmail(any(Email.class))).thenReturn(false);
        when(encoder.encrypt(any(Password.class))).thenReturn(encryptedPassword);
        when(repository.save(any(User.class))).thenReturn(savedUser);

        var result = useCase.execute(input);

        assertNotNull(result);
        verify(repository, times(1)).existsByEmail(any(Email.class));
        verify(encoder, times(1)).encrypt(any(Password.class));
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw DuplicateEmailError when email already exists")
    void shouldThrowDuplicateEmailExceptionWhenEmailAlreadyExists() {
        when(repository.existsByEmail(any(Email.class))).thenReturn(true);

        var exception = assertThrows(
                DuplicateEmailError.class,
                () -> useCase.execute(input)
        );

        assertNotNull(exception);
        verify(repository, times(1)).existsByEmail(any(Email.class));
        verify(encoder, never()).encrypt(any(Password.class));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should call methods in correct order")
    void shouldCallMethodsInCorrectOrder() {
        var encryptedPassword = Password.ofHash(ENCRYPTED_PASSWORD);

        when(repository.existsByEmail(any(Email.class))).thenReturn(false);
        when(encoder.encrypt(any(Password.class))).thenReturn(encryptedPassword);
        when(repository.save(any(User.class))).thenReturn(savedUser);

        useCase.execute(input);

        var inOrder = inOrder(repository, encoder);
        inOrder.verify(repository).existsByEmail(any(Email.class));
        inOrder.verify(encoder).encrypt(any(Password.class));
        inOrder.verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("Should encrypt password before creating user")
    void shouldEncryptPasswordBeforeCreatingUser() {
        var encryptedPassword = Password.ofHash(ENCRYPTED_PASSWORD);

        when(repository.existsByEmail(any(Email.class))).thenReturn(false);
        when(encoder.encrypt(any(Password.class))).thenReturn(encryptedPassword);
        when(repository.save(any(User.class))).thenReturn(savedUser);

        useCase.execute(input);

        verify(encoder, times(1)).encrypt(argThat(password ->
                password != null && password.getValue().equals(RAW_PASSWORD)
        ));
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should create user with correct value objects")
    void shouldCreateUserWithCorrectValueObjects() {
        var encryptedPassword = Password.ofHash(ENCRYPTED_PASSWORD);

        when(repository.existsByEmail(any(Email.class))).thenReturn(false);
        when(encoder.encrypt(any(Password.class))).thenReturn(encryptedPassword);
        when(repository.save(any(User.class))).thenReturn(savedUser);

        useCase.execute(input);

        verify(repository).existsByEmail(argThat(email ->
                email != null && email.getValue().equals(USER_EMAIL)
        ));
        verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("Should propagate exception when password encryption fails")
    void shouldPropagateExceptionWhenPasswordEncryptionFails() {
        when(repository.existsByEmail(any(Email.class))).thenReturn(false);
        when(encoder.encrypt(any(Password.class))).thenThrow(new ValidationError("Encryption failed"));

        assertThrows(ValidationError.class, () -> useCase.execute(input));
        verify(repository, times(1)).existsByEmail(any(Email.class));
        verify(encoder, times(1)).encrypt(any(Password.class));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should propagate exception when Email creation fails")
    void shouldPropagateExceptionWhenEmailCreationFails() {
        var invalidInput = new CreateUserInput(USER_NAME, "invalid-email", USER_TELEPHONE, RAW_PASSWORD);

        assertThrows(ValidationError.class, () -> useCase.execute(invalidInput));
        verify(repository, never()).existsByEmail(any(Email.class));
        verify(encoder, never()).encrypt(any(Password.class));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should propagate exception when Telephone creation fails")
    void shouldPropagateExceptionWhenTelephoneCreationFails() {
        var invalidInput = new CreateUserInput(USER_NAME, USER_EMAIL, "invalid", RAW_PASSWORD);

        assertThrows(ValidationError.class, () -> useCase.execute(invalidInput));
        verify(repository, never()).existsByEmail(any(Email.class));
        verify(encoder, never()).encrypt(any(Password.class));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should return UserOutput after saving user")
    void shouldReturnUserOutputAfterSavingUser() {
        var encryptedPassword = Password.ofHash(ENCRYPTED_PASSWORD);

        when(repository.existsByEmail(any(Email.class))).thenReturn(false);
        when(encoder.encrypt(any(Password.class))).thenReturn(encryptedPassword);
        when(repository.save(any(User.class))).thenReturn(savedUser);

        var result = useCase.execute(input);

        assertNotNull(result);
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should not save user when email already exists")
    void shouldNotSaveUserWhenEmailAlreadyExists() {
        when(repository.existsByEmail(any(Email.class))).thenReturn(true);

        assertThrows(DuplicateEmailError.class, () -> useCase.execute(input));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should check email existence before encrypting password")
    void shouldCheckEmailExistenceBeforeEncryptingPassword() {
        var encryptedPassword = Password.ofHash(ENCRYPTED_PASSWORD);

        when(repository.existsByEmail(any(Email.class))).thenReturn(false);
        when(encoder.encrypt(any(Password.class))).thenReturn(encryptedPassword);
        when(repository.save(any(User.class))).thenReturn(savedUser);

        useCase.execute(input);

        var inOrder = inOrder(repository, encoder);
        inOrder.verify(repository).existsByEmail(any(Email.class));
        inOrder.verify(encoder).encrypt(any(Password.class));
    }

    @Test
    @DisplayName("Should save user exactly once per execution")
    void shouldSaveUserExactlyOncePerExecution() {
        var encryptedPassword = Password.ofHash(ENCRYPTED_PASSWORD);

        when(repository.existsByEmail(any(Email.class))).thenReturn(false);
        when(encoder.encrypt(any(Password.class))).thenReturn(encryptedPassword);
        when(repository.save(any(User.class))).thenReturn(savedUser);

        useCase.execute(input);

        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should encrypt password exactly once per execution")
    void shouldEncryptPasswordExactlyOncePerExecution() {
        var encryptedPassword = Password.ofHash(ENCRYPTED_PASSWORD);

        when(repository.existsByEmail(any(Email.class))).thenReturn(false);
        when(encoder.encrypt(any(Password.class))).thenReturn(encryptedPassword);
        when(repository.save(any(User.class))).thenReturn(savedUser);

        useCase.execute(input);

        verify(encoder, times(1)).encrypt(any(Password.class));
    }
}