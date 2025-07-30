package com.productpulse.productpulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.productpulse.productpulse.model")
public class ProductpulseApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductpulseApplication.class, args);
		
	}

}
