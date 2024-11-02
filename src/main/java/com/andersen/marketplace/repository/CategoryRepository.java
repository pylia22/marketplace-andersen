package com.andersen.marketplace.repository;

import com.andersen.marketplace.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing categories.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    /**
     * Finds a category by its name.
     *
     * @param category the category name
     * @return the Category
     */
    Category findByName(String category);

    /**
     * Finds a category by its ID, including its products.
     *
     * @param id the category ID
     * @return an Optional containing the Category with its products, if found
     */
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.products WHERE c.id = :id")
    Optional<Category> findByIdWithProducts(UUID id);

    /**
     * Finds all categories, including their products.
     *
     * @return a list of Categories with their products
     */
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.products")
    List<Category> findAllWithProducts();

    /**
     * Finds all categories with pagination, including their products.
     *
     * @param pageable the pagination information
     * @return a page of Categories with their products
     */
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.products")
    Page<Category> findAllWithProducts(Pageable pageable);
}
