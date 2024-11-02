package com.andersen.marketplace.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity class representing a category.
 */
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column
    private String logo;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    /**
     * Constructs a new Category with the specified details.
     *
     * @param id the category ID
     * @param name the category name
     * @param logo the category logo
     * @param products the list of products in the category
     */
    public Category(UUID id, String name, String logo, List<Product> products) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.products = products;
    }

    /**
     * Default constructor for Category.
     */
    public Category() {
    }

    /**
     * Returns the category ID.
     *
     * @return the category ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the category ID.
     *
     * @param id the category ID
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Returns the category name.
     *
     * @return the category name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the category name.
     *
     * @param name the category name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the category logo.
     *
     * @return the category logo
     */
    public String getLogo() {
        return logo;
    }

    /**
     * Sets the category logo.
     *
     * @param logo the category logo
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * Returns the list of products in the category.
     *
     * @return the list of products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Sets the list of products in the category.
     *
     * @param products the list of products
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * Adds a product to the category.
     *
     * @param product the product to add
     */
    public void addProduct(Product product) {
        this.products.add(product);
    }

    /**
     * Checks if this category is equal to another object.
     *
     * @param o the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) &&
                Objects.equals(name, category.name) &&
                Objects.equals(logo, category.logo) &&
                Objects.equals(products, category.products);
    }

    /**
     * Returns the hash code of this category.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, logo);
    }
}
