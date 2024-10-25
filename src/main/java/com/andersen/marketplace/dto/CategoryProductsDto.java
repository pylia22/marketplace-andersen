package com.andersen.marketplace.dto;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CategoryProductsDto {

    private UUID id;
    private String name;
    private String logo;
    private List<ProductDto> products;

    public CategoryProductsDto(UUID id, String name, String logo, List<ProductDto> products) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.products = products;
    }

    public CategoryProductsDto(String name, String logo, List<ProductDto> products) {
        this.name = name;
        this.logo = logo;
        this.products = products;
    }

    public CategoryProductsDto() {
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

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryProductsDto that = (CategoryProductsDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(logo, that.logo) &&
                Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, logo, products);
    }
}
