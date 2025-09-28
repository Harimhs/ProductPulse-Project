package com.productpulse.productpulse.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private String productDescription;
    private String productLaunchDate;
    private String regionCountry;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }

    public String getProductLaunchDate() { return productLaunchDate; }
    public void setProductLaunchDate(String productLaunchDate) { this.productLaunchDate = productLaunchDate; }

    public String getRegionCountry() { return regionCountry; }
    public void setRegionCountry(String regionCountry) { this.regionCountry = regionCountry; }
}

