package com.andersen.marketplace.dto;

public class CategoryDto {

    private String name;
    private String logo;

    public CategoryDto(String name, String logo) {
        this.name = name;
        this.logo = logo;
    }

    public CategoryDto() {
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
}
