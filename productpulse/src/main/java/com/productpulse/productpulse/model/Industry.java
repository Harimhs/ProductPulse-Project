package com.productpulse.productpulse.model;

public enum Industry {
    AGRICULTURE("Agriculture"),
    AUTOMOBILES("Automobiles"),
    CONSTRUCTION("Construction"),
    ECOMMERCE("E-Commerce"),
    EDUCATION("Education"),
    ENERGY("Energy"),
    ENTERTAINMENT("Entertainment"),
    FINANCE("Finance"),
    FOOD_AND_BEVERAGE("Food & Beverage"),
    GADGETS("Gadgets"),
    HEALTHCARE("Healthcare"),
    HOSPITALITY("Hospitality"),
    MANUFACTURING("Manufacturing"),
    MARKETING("Marketing"),
    MEDIA("Media"),
    REAL_ESTATE("Real Estate"),
    RETAIL("Retail"),
    SALES("Sales"),
    TECHNOLOGY("Technology"),
    TELECOMMUNICATIONS("Telecommunications"),
    TRANSPORTATION("Transportation");

    private final String label;

    Industry(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
