package com.andersen.marketplace.dto;

import java.util.Objects;
import java.util.UUID;

public class ProductDto {

    private UUID id;
    private String name;
    private String logo;
    private String category;

    public ProductDto(UUID id, String name, String logo, String category) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.category = category;
    }

    public ProductDto(String name, String logo, String category) {
        this.name = name;
        this.logo = logo;
        this.category = category;
    }

    public ProductDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(logo, that.logo) && Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, logo, category);
    }
}
