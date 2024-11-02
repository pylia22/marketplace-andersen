package com.andersen.marketplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

/**
 * Entity class representing a user.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String name;
    private String password;
    private String roles;

    /**
     * Default constructor for User.
     */
    public User() {
    }

    /**
     * Returns the user ID.
     *
     * @return the user ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the user ID.
     *
     * @param id the user ID
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Returns the username.
     *
     * @return the username
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the username.
     *
     * @param name the username
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the user's password.
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the user's roles.
     *
     * @return the user's roles
     */
    public String getRoles() {
        return roles;
    }
}