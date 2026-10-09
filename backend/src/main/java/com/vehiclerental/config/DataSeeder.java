package com.vehiclerental.config;

import com.vehiclerental.entity.Role;
import com.vehiclerental.entity.User;
import com.vehiclerental.entity.enums.RoleName;
import com.vehiclerental.repository.RoleRepository;
import com.vehiclerental.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * RideRent System Initializer.
 * Strictly initializes required system security roles and default administrator account.
 * ZERO fake vehicles, demo users, mock bookings, or placeholder data seeded.
 */
@Component
public class DataSeeder implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Role roleUser = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleName.ROLE_USER).build()));
        Role roleAdmin = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleName.ROLE_ADMIN).build()));

        if (!userRepository.existsByEmail("admin@example.com")) {
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(roleUser);
            adminRoles.add(roleAdmin);

            userRepository.save(User.builder()
                    .fullName("Administrator")
                    .email("admin@example.com")
                    .phone(null)
                    .password(passwordEncoder.encode("admin123"))
                    .profilePhoto(null)
                    .isActive(true)
                    .roles(adminRoles)
                    .build());
            log.info("System administrator account provisioned (admin@example.com).");
        }

        log.info("RideRent initialized cleanly with zero fake data.");
    }
}
