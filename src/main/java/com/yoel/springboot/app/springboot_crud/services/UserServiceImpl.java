package com.yoel.springboot.app.springboot_crud.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.yoel.springboot.app.springboot_crud.entities.Role;
import com.yoel.springboot.app.springboot_crud.entities.User;
import com.yoel.springboot.app.springboot_crud.repositories.RoleRepository;
import com.yoel.springboot.app.springboot_crud.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
     @Transactional(readOnly = true)
    public Optional<User> findById(@NonNull Long id) {
        return (userRepository.findById(id));
    }

    @Override
    @Transactional
    public User register(User user) {

        Optional<Role> optionalRole = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();

        optionalRole.ifPresent(roles::add);

        user.setRoles(roles);
        String passwordEncoded =passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordEncoded);
        user.setAdmin(false);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(@NonNull Long id) {
       Optional<User> userOpt = userRepository.findById(id);
       userOpt.ifPresent(userRepository::delete);
    }

    @Override
    @Transactional
    public User update(@NonNull Long id, User user) {
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            existingUser.setUsername(user.getUsername());
            existingUser.setRoles(user.getRoles());
            return userRepository.save(existingUser);
        } else {
            return null;
        }
    }

    @Transactional
    public User changeRole(@NonNull Long id, Role role) {

        User user = userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Role roleDb = roleRepository.findByName(role.getName())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Rol no encontrado"));

            List<Role> roles = user.getRoles();
            
                if (!roles.contains(roleDb)) {
                    roles.add(roleDb);
                    user.setRoles(roles);
                }

                boolean isAdmin = roles.stream()
                .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));
                user.setAdmin(isAdmin);
            return userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);   
    }
    
}
