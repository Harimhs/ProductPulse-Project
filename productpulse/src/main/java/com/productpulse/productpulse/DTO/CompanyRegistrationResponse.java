package com.productpulse.productpulse.DTO;

import com.productpulse.productpulse.model.Company;
import lombok.Getter;

@Getter
public class CompanyRegistrationResponse {
    private String message;
    private Company company;
    private String token;

    public CompanyRegistrationResponse(String message, Company company, String token) {
        this.message = message;
        this.company = company;
        this.token = token;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setToken(String token) {
        this.token = token;
    }
}