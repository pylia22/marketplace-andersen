package com.andersen.marketplace;

import com.andersen.marketplace.cache.GenericCache;
import com.andersen.marketplace.entity.Category;
import com.andersen.marketplace.entity.Product;
import com.andersen.marketplace.repository.CategoryRepository;
import com.andersen.marketplace.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataLoader implements CommandLineRunner {

    private final GenericCache<UUID, Product> productCache;
    private final GenericCache<UUID, Category> categoryCache;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public DataLoader(GenericCache<UUID, Product> productCache, GenericCache<UUID, Category> categoryCache,
                      ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productCache = productCache;
        this.categoryCache = categoryCache;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        loadProductsFromDataSource();
        loadCategoriesFromDataSource();
    }

    private void loadProductsFromDataSource() {
        productRepository.findAll().forEach(product -> productCache.put(product.getId(), product));
    }

    private void loadCategoriesFromDataSource() {
        categoryRepository.findAllWithProducts().forEach(category -> categoryCache.put(category.getId(), category));
    }
}
