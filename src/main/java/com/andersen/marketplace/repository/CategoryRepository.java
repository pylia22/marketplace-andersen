package com.andersen.marketplace.repository;

import com.andersen.marketplace.dto.ProductDto;
import com.andersen.marketplace.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String category);
}
