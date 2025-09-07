package com.productpulse.productpulse.service;

public class CompanyAlreadyExistsException extends RuntimeException{
    public CompanyAlreadyExistsException(String message){
        super(message);
    }
}
