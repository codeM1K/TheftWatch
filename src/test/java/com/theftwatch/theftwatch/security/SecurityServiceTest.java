package com.theftwatch.theftwatch.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityServiceTest {

    @Test
    void passwordEncoderShouldEncodePassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "testpassword";
        String encodedPassword = encoder.encode(rawPassword);

        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }
}
