package com.productpulse.productpulse.service;

public class CompanyExistsException extends RuntimeException{
    public CompanyExistsException(String message){
        super(message);
    }
}
