package com.yoel.springboot.app.springboot_crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.yoel.springboot.app.springboot_crud.entities.Product;
import com.yoel.springboot.app.springboot_crud.repositories.ProductRepository;





@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return (List<Product>)productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(@NonNull Long id) {
        return productRepository.findById( id);
    }

    @Override
    public Product save(@NonNull Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(@NonNull Long id) {
        Optional<Product> productOpt = productRepository.findById(id);  
        productOpt.ifPresent(productRepository::delete); 
    }

    @Override
    public Product update(@NonNull Long id, Product product) {
        Optional<Product> existingproductOpt = productRepository.findById(id);
        if (existingproductOpt.isPresent()) {
            Product productUpdated = existingproductOpt.get();
            productUpdated.setName(product.getName());
            productUpdated.setPrice(product.getPrice());
            productUpdated.setDescription(product.getDescription());
        
            return productRepository.save(productUpdated);
        } else {
            return null;
        }
        
    }

}
