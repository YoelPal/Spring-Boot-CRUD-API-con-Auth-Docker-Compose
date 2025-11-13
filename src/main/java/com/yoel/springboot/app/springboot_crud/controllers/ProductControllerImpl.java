package com.yoel.springboot.app.springboot_crud.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.yoel.springboot.app.springboot_crud.entities.Product;
import com.yoel.springboot.app.springboot_crud.services.GenericService;

@RestController
@RequestMapping("/api/products")
public class ProductControllerImpl extends GenericController<Product> {

    public ProductControllerImpl(GenericService<Product> genericService) {
        super(genericService);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @PostMapping
    public ResponseEntity<Product> createProduct(@NonNull @RequestBody Product entity) {
        return super.createProduct(entity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntity(@NonNull @PathVariable Long id) {
        return super.deleteEntity(id);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<Product>> getAllEntity() {
        return super.getAllEntity();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Product> getEntityById(@NonNull @PathVariable Long id) {
        return super.getEntityById(id);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateEntity(@NonNull @PathVariable Long id, @RequestBody Product entity) {
        return super.updateEntity(id, entity);
    }

}
