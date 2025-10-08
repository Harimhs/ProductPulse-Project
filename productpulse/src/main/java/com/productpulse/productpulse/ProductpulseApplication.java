package com.productpulse.productpulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EntityScan(basePackages = "com.productpulse.productpulse.model")
public class ProductpulseApplication {

	public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("spring.mail.username", dotenv.get("SMTP_USERNAME"));
        System.setProperty("spring.mail.password", dotenv.get("SMTP_PASSWORD"));
        System.setProperty("SECRET_KEY", dotenv.get("SECRET_KEY"));
        SpringApplication.run(ProductpulseApplication.class, args);
    }

}
