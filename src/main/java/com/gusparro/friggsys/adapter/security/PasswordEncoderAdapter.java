package com.gusparro.friggsys.adapter.security;

import com.gusparro.friggsys.domain.vos.Password;
import com.gusparro.friggsys.usecase.user.services.PasswordEncoderService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderAdapter implements PasswordEncoderService {

    private final BCryptPasswordEncoder encoder;

    public PasswordEncoderAdapter() {
        this.encoder = new BCryptPasswordEncoder();
    }

    @Override
    public Password encrypt(Password rawPassword) {
        String hash = encoder.encode(rawPassword.getValue());

        return Password.ofHash(hash);
    }

    @Override
    public boolean matches(String rawPassword, String encryptedPassword) {
        return encoder.matches(rawPassword, encryptedPassword);
    }

}
