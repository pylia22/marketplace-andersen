package com.andersen.marketplace.repository;

import com.andersen.marketplace.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

/**
 * Repository interface for managing products.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    /**
     * Finds unique product names.
     *
     * @return a set of unique product names
     */
    @Query("select distinct p.name from Product p")
    Set<String> findUniqueProducts();

    /**
     * Finds all products with optional filtering by category and name.
     *
     * @param category the category name to filter by (optional)
     * @param name the product name to filter by (optional)
     * @param pageable the pagination information
     * @return a page of products matching the filter criteria
     */
    @Query("""
SELECT p
FROM Product p
WHERE (:category IS NULL OR p.category.name ILIKE %:category%)
AND (:name IS NULL OR p.name ILIKE %:name%)
""")
    Page<Product> findAllWithFilter(@Param("category") String category, @Param("name") String name, Pageable pageable);
}
