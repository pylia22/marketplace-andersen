package com.andersen.marketplace.dto;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDto that = (ProductDto) o;
        return Objects.equals(name, that.name) && Objects.equals(logo, that.logo) && Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, logo, category);
    }
}
