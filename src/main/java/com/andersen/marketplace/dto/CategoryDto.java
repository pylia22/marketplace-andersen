package com.andersen.marketplace.dto;

import java.util.List;

public class CategoryDto {

    private String name;
    private String logo;
    private List<ProductDto> products;

    public CategoryDto(String name, String logo, List<ProductDto> products) {
        this.name = name;
        this.logo = logo;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }
}
