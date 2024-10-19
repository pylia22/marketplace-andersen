package com.andersen.marketplace.dto;

public class ProductSearchRequest {

    private String productCategory;
    private String productName;

    public ProductSearchRequest(String productCategory, String productName) {
        this.productCategory = productCategory;
        this.productName = productName;
    }

    public ProductSearchRequest() {
    }

    public String getProductCategory() {
        return productCategory;
    }

    public String getProductName() {
        return productName;
    }
}
