package com.andersen.marketplace.repository;

import com.andersen.marketplace.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select distinct p.name from Product p")
    Set<String> findUniqueProducts();

    @Query("SELECT p FROM Product p WHERE " +
            "(:category IS NULL OR LOWER(p.category.name) = LOWER(:category)) AND " +
            "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<Product> findAllWithFilter(@Param("category") String category, @Param("name") String name, Pageable pageable);
}
