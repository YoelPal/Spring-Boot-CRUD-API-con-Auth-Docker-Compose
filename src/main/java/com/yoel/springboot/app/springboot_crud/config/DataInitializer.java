package com.yoel.springboot.app.springboot_crud.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.yoel.springboot.app.springboot_crud.entities.Role;
import com.yoel.springboot.app.springboot_crud.entities.User;
import com.yoel.springboot.app.springboot_crud.repositories.RoleRepository;
import com.yoel.springboot.app.springboot_crud.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class DataInitializer {

    private static final String ADMIN_PASSWORD_RAW = "admin123";
    private static final String ADMIN_USERNAME = "admin";
    private static final String USER_USERNAME = "user";
    private static final String ADMIN_ROLE_NAME = "ROLE_ADMIN";
    private static final String USER_ROLE_NAME = "ROLE_USER";

    @Bean
    public CommandLineRunner dataInitializerStart(UserRepository userRepository, 
        RoleRepository roleRepository,
        PasswordEncoder passwordEncoder) {
        
        return args -> {
            if(userRepository.findByUsername(ADMIN_USERNAME).isEmpty()) {
                var adminRole = roleRepository.findByName(ADMIN_ROLE_NAME)
                        .orElseGet(() -> {
                            Role roleAdmin = new Role();
                            roleAdmin.setName(ADMIN_ROLE_NAME);
                            return roleRepository.save(roleAdmin);
                        });
            if(userRepository.findByUsername(USER_USERNAME).isEmpty()) {
                var userRole = roleRepository.findByName(USER_ROLE_NAME)
                        .orElseGet(() -> {
                            Role roleUser = new Role();
                            roleUser.setName(USER_ROLE_NAME);
                            return roleRepository.save(roleUser);
                        });

                User adminUser = new User();
                adminUser.setUsername(ADMIN_USERNAME);
                adminUser.setPassword(passwordEncoder.encode(ADMIN_PASSWORD_RAW));
            
                Optional<Role> optionalRole = roleRepository.findByName("ROLE_USER");
                List<Role> roles = new ArrayList<>();

                roles.add(adminRole);

                adminUser.setRoles(roles);
                userRepository.save(adminUser);
                log.info("Admin creado con username: {} y password: {}", ADMIN_USERNAME, ADMIN_PASSWORD_RAW);
            }else {
                log.info("El usuario admin ya existe, no se creará de nuevo.");
            }
        
            
        }
    };
    
}
}


