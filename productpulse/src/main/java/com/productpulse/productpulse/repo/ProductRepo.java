package com.productpulse.productpulse.repo;

import com.productpulse.productpulse.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Long> {
}

