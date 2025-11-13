package com.yoel.springboot.app.springboot_crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;

import com.yoel.springboot.app.springboot_crud.entities.Role;
import com.yoel.springboot.app.springboot_crud.entities.User;

public interface UserService {

    List<User> findAll();
    Optional<User> findById(@NonNull Long id);
    User register(User user);
    void deleteById(@NonNull Long id);
    User update(@NonNull Long id,  User user);
    User changeRole(@NonNull Long id, Role role);
    boolean existsByUsername(String username);
    


}
