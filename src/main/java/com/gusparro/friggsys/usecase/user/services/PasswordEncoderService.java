package com.gusparro.friggsys.usecase.user.services;

import com.gusparro.friggsys.domain.vos.Password;

public interface PasswordEncoderService {

    Password encrypt(Password rawPassword);

    boolean matches(String rawPassword, String encryptedPassword);

}
