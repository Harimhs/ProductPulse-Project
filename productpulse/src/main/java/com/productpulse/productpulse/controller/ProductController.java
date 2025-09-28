package com.productpulse.productpulse.controller;

import com.productpulse.productpulse.model.Product;
import com.productpulse.productpulse.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product); // saves in DB
        return savedProduct;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
}
