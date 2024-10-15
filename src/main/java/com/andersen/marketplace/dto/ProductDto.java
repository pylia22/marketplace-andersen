package com.andersen.marketplace.dto;

public class ProductDto {

    private String name;
    private String logo;
    private String category;

    public ProductDto(String name, String logo, String category) {
        this.name = name;
        this.logo = logo;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
