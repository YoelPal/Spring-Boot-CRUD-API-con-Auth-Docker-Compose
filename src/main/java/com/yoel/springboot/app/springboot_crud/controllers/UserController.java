package com.yoel.springboot.app.springboot_crud.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yoel.springboot.app.springboot_crud.entities.Role;
import com.yoel.springboot.app.springboot_crud.entities.User;
import com.yoel.springboot.app.springboot_crud.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> list() {
        return userService.findAll();
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> changeRol(@PathVariable Long id, @RequestBody Role role) {
        User user = userService.changeRole(id, role);
        return ResponseEntity.ok(user);
    }


}
