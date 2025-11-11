package com.yoel.springboot.app.springboot_crud.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.yoel.springboot.app.springboot_crud.entities.Product;
import com.yoel.springboot.app.springboot_crud.services.GenericService;

@RestController
@RequestMapping("/products")
public class ProductControllerImpl extends GenericController<Product> {

    public ProductControllerImpl(GenericService<Product> genericService) {
        super(genericService);
    }

    @Override
    @PostMapping
    public ResponseEntity<Product> createProduct( @RequestBody Product entity) {
        return super.createProduct(entity);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable Long id) {
        return super.deleteEntity(id);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<Product>> getAllEntity() {
        return super.getAllEntity();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Product> getEntityById(@PathVariable Long id) {
        return super.getEntityById(id);
    }

}
