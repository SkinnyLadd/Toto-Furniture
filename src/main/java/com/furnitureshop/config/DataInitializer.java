package com.furnitureshop.config;

import com.furnitureshop.model.entity.Role;
import com.furnitureshop.model.entity.User;
import com.furnitureshop.repository.RoleRepository;
import com.furnitureshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only initialize if no users exist
        if (userRepository.count() == 0) {
            // Create roles if they don't exist
            createRoleIfNotFound("ROLE_ADMIN", "Administrator with full access");
            createRoleIfNotFound("ROLE_MANAGER", "Manager with access to most features");
            createRoleIfNotFound("ROLE_SALES", "Sales staff with access to sales features");
            createRoleIfNotFound("ROLE_TECHNICIAN", "Technician with access to inventory and refurbishment");

            // Create admin user
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setFullName("System Administrator");
            adminUser.setEmail("admin@furnitureshop.com");
            adminUser.setPhoneNumber("03001234567");
            adminUser.setActive(true);

            // Assign admin role
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            adminUser.setRoles(roles);

            userRepository.save(adminUser);

            System.out.println("Admin user created successfully with username: admin and password: admin123");
        }
    }

    private void createRoleIfNotFound(String name, String description) {
        roleRepository.findByName(name).orElseGet(() -> {
            Role role = new Role();
            role.setName(name);
            role.setDescription(description);
            return roleRepository.save(role);
        });
    }
}
