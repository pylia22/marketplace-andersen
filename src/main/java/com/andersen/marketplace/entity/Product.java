package com.andersen.marketplace.entity;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

/**
 * Entity class representing a product.
 */
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column
    private String logo;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * Constructs a new Product with the specified details.
     *
     * @param id the product ID
     * @param name the product name
     * @param logo the product logo
     * @param category the product category
     */
    public Product(UUID id, String name, String logo, Category category) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.category = category;
    }

    /**
     * Constructs a new Product with the specified details.
     *
     * @param name the product name
     * @param logo the product logo
     * @param category the product category
     */
    public Product(String name, String logo, Category category) {
        this.name = name;
        this.logo = logo;
        this.category = category;
    }

    /**
     * Constructs a new Product with the specified details.
     *
     * @param name the product name
     * @param logo the product logo
     */
    public Product(String name, String logo) {
        this.name = name;
        this.logo = logo;
    }

    /**
     * Default constructor for Product.
     */
    public Product() {
    }

    /**
     * Returns the product ID.
     *
     * @return the product ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the product ID.
     *
     * @param id the product ID
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Returns the product name.
     *
     * @return the product name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the product name.
     *
     * @param name the product name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the product logo.
     *
     * @return the product logo
     */
    public String getLogo() {
        return logo;
    }

    /**
     * Sets the product logo.
     *
     * @param logo the product logo
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * Returns the product category.
     *
     * @return the product category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets the product category.
     *
     * @param category the product category
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Checks if this product is equal to another object.
     *
     * @param o the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(logo, product.logo) &&
                Objects.equals(category, product.category);
    }

    /**
     * Returns the hash code of this product.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, logo, category);
    }
}
