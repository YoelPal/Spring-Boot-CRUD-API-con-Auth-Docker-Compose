package com.yoel.springboot.app.springboot_crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;



public interface GenericService<T> {

    List<T> findAll();
    Optional<T> findById(@NonNull Long id);
    T save(@NonNull T t);
    void delete(@NonNull Long id);
    T update(@NonNull Long id, T t);

}
