package com.example.lyra.config;

import com.example.lyra.model.ERole;
import com.example.lyra.model.Role;
import com.example.lyra.model.User;
import com.example.lyra.repository.RoleRepository;
import com.example.lyra.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public CommandLineRunner initDatabase(RoleRepository roleRepository, 
                                         UserRepository userRepository,
                                         PasswordEncoder passwordEncoder) {
        return args -> {
            // Create roles if they don't exist
            if (roleRepository.count() == 0) {
                Role adminRole = new Role();
                adminRole.setName(ERole.ROLE_ADMIN);
                roleRepository.save(adminRole);

                Role userRole = new Role();
                userRole.setName(ERole.ROLE_USER);
                roleRepository.save(userRole);
            }

            // Create admin user if not exists
            if (!userRepository.existsByEmail("admin@example.com")) {
                User admin = new User();
                admin.setFirstName("Admin");
                admin.setLastName("User");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                
                Set<Role> roles = new HashSet<>();
                Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(adminRole);
                
                admin.setRoles(roles);
                userRepository.save(admin);
            }
        };
    }
}
