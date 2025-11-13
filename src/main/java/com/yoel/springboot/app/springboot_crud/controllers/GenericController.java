package com.yoel.springboot.app.springboot_crud.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import com.yoel.springboot.app.springboot_crud.services.GenericService;

public abstract class GenericController<T> {

    private final GenericService <T> genericService;

    protected GenericController(GenericService<T> genericService) {
        this.genericService = genericService;
    }

    public ResponseEntity<List<T>> getAllEntity(){
        List<T> lista = genericService.findAll();
        return ResponseEntity.ok(lista);
    }
    public ResponseEntity<T> getEntityById(@NonNull Long id){
        Optional<T> productOpt = genericService.findById(id);
        return productOpt.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    public ResponseEntity<T> createProduct(@NonNull T entity){
         T entityT = genericService.save(entity);
         return ResponseEntity.ok(entityT);
    
    }
    public ResponseEntity<Void> deleteEntity(@NonNull Long id){
        Optional<T> entityOpt = genericService.findById(id);
        if(entityOpt.isPresent()){
            genericService.delete(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    public ResponseEntity<T> updateEntity(@NonNull Long id, T entity){
        Optional<T> entityOpt = genericService.findById(id);
        if(entityOpt.isPresent()){
            T entityUpdated = genericService.update(id, entity);
            return ResponseEntity.ok(entityUpdated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
