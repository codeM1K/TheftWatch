package com.theftwatch.theftwatch.config;

import com.theftwatch.theftwatch.domain.User;
import com.theftwatch.theftwatch.domain.enums.Role;
import com.theftwatch.theftwatch.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setEmail("admin@theftwatch.local");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setFullName("Default Super Admin");
                admin.setRole(Role.SUPER_ADMIN);
                admin.setEnabled(true);
                userRepository.save(admin);
            }
        };
    }
}
