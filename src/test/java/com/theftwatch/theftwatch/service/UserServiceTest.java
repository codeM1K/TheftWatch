package com.theftwatch.theftwatch.service;

import com.theftwatch.theftwatch.domain.User;
import com.theftwatch.theftwatch.domain.enums.Role;
import com.theftwatch.theftwatch.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Test
    void createUserShouldEncodePassword() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.createUser("test@example.com", "password123", "Test User", Role.END_USER, null);

        assertNotNull(user);
        assertNotNull(user.getPassword());
        verify(passwordEncoder).encode("password123");
    }

    @Test
    void findByEmailShouldReturnUser() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);

        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        User found = userService.findByEmail("test@example.com");
        assertNotNull(found);
        assertEquals("test@example.com", found.getEmail());
    }
}
