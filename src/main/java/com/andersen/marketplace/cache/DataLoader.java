package com.andersen.marketplace.cache;

import com.andersen.marketplace.entity.Category;
import com.andersen.marketplace.entity.Product;
import com.andersen.marketplace.repository.CategoryRepository;
import com.andersen.marketplace.repository.ProductRepository;
import com.andersen.marketplace.service.PictureServiceImpl;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataLoader {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

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

    @PostConstruct
    public void loadData() {
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
