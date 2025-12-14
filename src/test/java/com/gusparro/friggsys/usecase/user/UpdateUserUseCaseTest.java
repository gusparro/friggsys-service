package com.gusparro.friggsys.usecase.user;

import com.gusparro.friggsys.domain.entities.User;
import com.gusparro.friggsys.domain.exceptions.ValidationError;
import com.gusparro.friggsys.domain.repositories.UserRepositoryInterface;
import com.gusparro.friggsys.domain.vos.Email;
import com.gusparro.friggsys.domain.vos.Name;
import com.gusparro.friggsys.domain.vos.Telephone;
import com.gusparro.friggsys.usecase.exceptions.DuplicateEmailError;
import com.gusparro.friggsys.usecase.exceptions.EntityNotFoundError;
import com.gusparro.friggsys.usecase.user.dtos.UpdateUserInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateUserUseCase Tests")
class UpdateUserUseCaseTest {

    @Mock
    private UserRepositoryInterface repository;

    @InjectMocks
    private UpdateUserUseCase useCase;

    @Mock
    private User user;

    @Mock
    private User updatedUser;

    private UUID userId;
    private UpdateUserInput input;
    private static final String USER_NAME = "John Doe Updated";
    private static final String USER_EMAIL = "john.updated@example.com";
    private static final String CURRENT_USER_EMAIL = "john.current@example.com";
    private static final String USER_TELEPHONE = "(11) 98888-8888";

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        input = new UpdateUserInput(userId, USER_NAME, USER_EMAIL, USER_TELEPHONE);
    }

    @Test
    @DisplayName("Should update user successfully when user exists and email is not duplicated")
    void shouldUpdateUserSuccessfullyWhenUserExistsAndEmailIsNotDuplicated() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.existsByEmail(any(Email.class))).thenReturn(false);
        doNothing().when(user).update(any(Name.class), any(Email.class), any(Telephone.class));
        when(repository.save(user)).thenReturn(updatedUser);

        var result = useCase.execute(input);

        assertNotNull(result);
        verify(repository, times(1)).findById(userId);
        verify(repository, times(1)).existsByEmail(any(Email.class));
        verify(user, times(1)).update(any(Name.class), any(Email.class), any(Telephone.class));
        verify(repository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should update user successfully when keeping the same email")
    void shouldUpdateUserSuccessfullyWhenKeepingTheSameEmail() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.existsByEmail(any(Email.class))).thenReturn(true);
        when(user.getEmail()).thenReturn(USER_EMAIL);
        doNothing().when(user).update(any(Name.class), any(Email.class), any(Telephone.class));
        when(repository.save(user)).thenReturn(updatedUser);

        var result = useCase.execute(input);

        assertNotNull(result);
        verify(repository, times(1)).findById(userId);
        verify(repository, times(1)).existsByEmail(any(Email.class));
        verify(user, times(1)).getEmail();
        verify(user, times(1)).update(any(Name.class), any(Email.class), any(Telephone.class));
        verify(repository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundError when user does not exist")
    void shouldThrowEntityNotFoundErrorWhenUserDoesNotExist() {
        when(repository.findById(userId)).thenReturn(Optional.empty());

        var exception = assertThrows(
                EntityNotFoundError.class,
                () -> useCase.execute(input)
        );

        assertNotNull(exception);
        verify(repository, times(1)).findById(userId);
        verify(repository, never()).existsByEmail(any(Email.class));
        verify(user, never()).getEmail();
        verify(user, never()).update(any(Name.class), any(Email.class), any(Telephone.class));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw DuplicateEmailError when email already exists for different user")
    void shouldThrowDuplicateEmailErrorWhenEmailAlreadyExistsForDifferentUser() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.existsByEmail(any(Email.class))).thenReturn(true);
        when(user.getEmail()).thenReturn(CURRENT_USER_EMAIL);

        var exception = assertThrows(
                DuplicateEmailError.class,
                () -> useCase.execute(input)
        );

        assertNotNull(exception);
        verify(repository, times(1)).findById(userId);
        verify(repository, times(1)).existsByEmail(any(Email.class));
        verify(user, times(1)).getEmail();
        verify(user, never()).update(any(Name.class), any(Email.class), any(Telephone.class));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should call methods in correct order when email is different")
    void shouldCallMethodsInCorrectOrderWhenEmailIsDifferent() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.existsByEmail(any(Email.class))).thenReturn(false);
        doNothing().when(user).update(any(Name.class), any(Email.class), any(Telephone.class));
        when(repository.save(user)).thenReturn(updatedUser);

        useCase.execute(input);

        var inOrder = inOrder(repository, user);
        inOrder.verify(repository).findById(userId);
        inOrder.verify(repository).existsByEmail(any(Email.class));
        inOrder.verify(user).update(any(Name.class), any(Email.class), any(Telephone.class));
        inOrder.verify(repository).save(user);
    }

    @Test
    @DisplayName("Should call methods in correct order when email is the same")
    void shouldCallMethodsInCorrectOrderWhenEmailIsTheSame() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.existsByEmail(any(Email.class))).thenReturn(true);
        when(user.getEmail()).thenReturn(USER_EMAIL);
        doNothing().when(user).update(any(Name.class), any(Email.class), any(Telephone.class));
        when(repository.save(user)).thenReturn(updatedUser);

        useCase.execute(input);

        var inOrder = inOrder(repository, user);
        inOrder.verify(repository).findById(userId);
        inOrder.verify(repository).existsByEmail(any(Email.class));
        inOrder.verify(user).getEmail();
        inOrder.verify(user).update(any(Name.class), any(Email.class), any(Telephone.class));
        inOrder.verify(repository).save(user);
    }

    @Test
    @DisplayName("Should create value objects with correct values")
    void shouldCreateValueObjectsWithCorrectValues() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.existsByEmail(any(Email.class))).thenReturn(false);
        doNothing().when(user).update(any(Name.class), any(Email.class), any(Telephone.class));
        when(repository.save(user)).thenReturn(updatedUser);

        useCase.execute(input);

        verify(user).update(
                argThat(name -> name != null && name.getValue().equals(USER_NAME)),
                argThat(email -> email != null && email.getValue().equals(USER_EMAIL)),
                argThat(telephone -> telephone != null && telephone.getValue().equals(USER_TELEPHONE))
        );
    }

    @Test
    @DisplayName("Should propagate ValidationError when Name creation fails")
    void shouldPropagateValidationErrorWhenNameCreationFails() {
        var invalidInput = new UpdateUserInput(userId, "", USER_EMAIL, USER_TELEPHONE);
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(ValidationError.class, () -> useCase.execute(invalidInput));
        verify(repository, times(1)).findById(userId);
        verify(repository, never()).existsByEmail(any(Email.class));
        verify(user, never()).getEmail();
        verify(user, never()).update(any(Name.class), any(Email.class), any(Telephone.class));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should propagate ValidationError when Email creation fails")
    void shouldPropagateValidationErrorWhenEmailCreationFails() {
        var invalidInput = new UpdateUserInput(userId, USER_NAME, "invalid-email", USER_TELEPHONE);
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(ValidationError.class, () -> useCase.execute(invalidInput));
        verify(repository, times(1)).findById(userId);
        verify(repository, never()).existsByEmail(any(Email.class));
        verify(user, never()).getEmail();
        verify(user, never()).update(any(Name.class), any(Email.class), any(Telephone.class));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should propagate ValidationError when Telephone creation fails")
    void shouldPropagateValidationErrorWhenTelephoneCreationFails() {
        var invalidInput = new UpdateUserInput(userId, USER_NAME, USER_EMAIL, "invalid");
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(ValidationError.class, () -> useCase.execute(invalidInput));
        verify(repository, times(1)).findById(userId);
        verify(repository, never()).existsByEmail(any(Email.class));
        verify(user, never()).getEmail();
        verify(user, never()).update(any(Name.class), any(Email.class), any(Telephone.class));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should return UserOutput after saving updated user")
    void shouldReturnUserOutputAfterSavingUpdatedUser() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.existsByEmail(any(Email.class))).thenReturn(false);
        doNothing().when(user).update(any(Name.class), any(Email.class), any(Telephone.class));
        when(repository.save(user)).thenReturn(updatedUser);

        var result = useCase.execute(input);

        assertNotNull(result);
        verify(repository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should not save user when email already exists for different user")
    void shouldNotSaveUserWhenEmailAlreadyExistsForDifferentUser() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.existsByEmail(any(Email.class))).thenReturn(true);
        when(user.getEmail()).thenReturn(CURRENT_USER_EMAIL);

        assertThrows(DuplicateEmailError.class, () -> useCase.execute(input));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should check email existence after finding user")
    void shouldCheckEmailExistenceAfterFindingUser() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.existsByEmail(any(Email.class))).thenReturn(false);
        doNothing().when(user).update(any(Name.class), any(Email.class), any(Telephone.class));
        when(repository.save(user)).thenReturn(updatedUser);

        useCase.execute(input);

        var inOrder = inOrder(repository);
        inOrder.verify(repository).findById(userId);
        inOrder.verify(repository).existsByEmail(any(Email.class));
    }

    @Test
    @DisplayName("Should save user exactly once per execution")
    void shouldSaveUserExactlyOncePerExecution() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.existsByEmail(any(Email.class))).thenReturn(false);
        doNothing().when(user).update(any(Name.class), any(Email.class), any(Telephone.class));
        when(repository.save(user)).thenReturn(updatedUser);

        useCase.execute(input);

        verify(repository, times(1)).save(user);
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should update user before saving")
    void shouldUpdateUserBeforeSaving() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.existsByEmail(any(Email.class))).thenReturn(false);
        doNothing().when(user).update(any(Name.class), any(Email.class), any(Telephone.class));
        when(repository.save(user)).thenReturn(updatedUser);

        useCase.execute(input);

        var inOrder = inOrder(user, repository);
        inOrder.verify(user).update(any(Name.class), any(Email.class), any(Telephone.class));
        inOrder.verify(repository).save(user);
    }

    @Test
    @DisplayName("Should verify email with correct value object")
    void shouldVerifyEmailWithCorrectValueObject() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.existsByEmail(any(Email.class))).thenReturn(false);
        doNothing().when(user).update(any(Name.class), any(Email.class), any(Telephone.class));
        when(repository.save(user)).thenReturn(updatedUser);

        useCase.execute(input);

        verify(repository).existsByEmail(argThat(email ->
                email != null && email.getValue().equals(USER_EMAIL)
        ));
    }

    @Test
    @DisplayName("Should check if email exists before comparing with user email")
    void shouldCheckIfEmailExistsBeforeComparingWithUserEmail() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.existsByEmail(any(Email.class))).thenReturn(true);
        when(user.getEmail()).thenReturn(USER_EMAIL);
        doNothing().when(user).update(any(Name.class), any(Email.class), any(Telephone.class));
        when(repository.save(user)).thenReturn(updatedUser);

        useCase.execute(input);

        var inOrder = inOrder(repository, user);
        inOrder.verify(repository).existsByEmail(any(Email.class));
        inOrder.verify(user).getEmail();
    }

    @Test
    @DisplayName("Should allow update with same email when email belongs to same user")
    void shouldAllowUpdateWithSameEmailWhenEmailBelongsToSameUser() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.existsByEmail(any(Email.class))).thenReturn(true);
        when(user.getEmail()).thenReturn(USER_EMAIL);
        doNothing().when(user).update(any(Name.class), any(Email.class), any(Telephone.class));
        when(repository.save(user)).thenReturn(updatedUser);

        var result = useCase.execute(input);

        assertNotNull(result);
        verify(repository, times(1)).existsByEmail(any(Email.class));
        verify(user, times(1)).getEmail();
        verify(user, times(1)).update(any(Name.class), any(Email.class), any(Telephone.class));
        verify(repository, times(1)).save(user);
    }

}