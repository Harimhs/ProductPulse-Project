package com.productpulse.productpulse.service;

// ProductService.java

import com.productpulse.productpulse.model.Product;
import com.productpulse.productpulse.repo.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepo productRepository;


    public ProductService(ProductRepo productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}

