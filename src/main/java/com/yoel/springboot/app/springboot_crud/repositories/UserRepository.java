package com.yoel.springboot.app.springboot_crud.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import com.yoel.springboot.app.springboot_crud.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    @Override
    @EntityGraph(attributePaths = {"roles"}) //Precarga los roles asociados a cada usuario evita N + 1
    @NonNull
    List<User> findAll();
    
    Optional<User> findByUsername(String username);


}
