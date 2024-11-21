package com.andersen.marketplace.cache;

import com.andersen.marketplace.entity.Category;
import com.andersen.marketplace.entity.Product;
import com.andersen.marketplace.repository.CategoryRepository;
import com.andersen.marketplace.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * DataLoader is a Spring component responsible for loading data into caches
 * from the data source repositories upon application startup.
 */
@Component
public class DataLoader {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final GenericCache<UUID, Product> productCache;
    private final GenericCache<UUID, Category> categoryCache;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Constructs a DataLoader with the specified caches and repositories.
     *
     * @param productCache the cache for storing products
     * @param categoryCache the cache for storing categories
     * @param productRepository the repository for accessing product data
     * @param categoryRepository the repository for accessing category data
     */
    public DataLoader(GenericCache<UUID, Product> productCache, GenericCache<UUID, Category> categoryCache,
                      ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productCache = productCache;
        this.categoryCache = categoryCache;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Loads data into the caches from the data source repositories.
     * This method is called after the bean's properties have been set.
     */
    @PostConstruct
    public void loadData() {
        logger.info("Starting to load data...");
        loadProductsFromDataSource();
        loadCategoriesFromDataSource();
        logger.info("Data loading completed.");
    }

    /**
     * Loads products from the data source and stores them in the product cache.
     */
    private void loadProductsFromDataSource() {
        productRepository.findAll().forEach(product -> {
            productCache.put(product.getId(), product);
            logger.debug("Loaded product: {}", product);
        });
    }

    /**
     * Loads categories from the data source and stores them in the category cache.
     */
    private void loadCategoriesFromDataSource() {
        categoryRepository.findAllWithProducts().forEach(category -> {
            categoryCache.put(category.getId(), category);
            logger.debug("Loaded category: {}", category);
        });
    }
}
