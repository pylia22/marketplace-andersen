package com.andersen.marketplace.service;

import com.andersen.marketplace.cache.GenericCache;
import com.andersen.marketplace.dto.CategoryDto;
import com.andersen.marketplace.dto.CategoryProductsDto;
import com.andersen.marketplace.dto.ProductDto;
import com.andersen.marketplace.entity.Category;
import com.andersen.marketplace.entity.Product;
import com.andersen.marketplace.exception.CategoryNotFoundException;
import com.andersen.marketplace.exception.DuplicatedCategoryException;
import com.andersen.marketplace.mapper.CategoryMapper;
import com.andersen.marketplace.mapper.ProductMapper;
import com.andersen.marketplace.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Service class for managing categories.
 */
@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;
    private final PictureService pictureService;
    private final GenericCache<UUID, Category> cache;

    /**
     * Constructs a new CategoryService.
     *
     * @param categoryRepository the category repository
     * @param categoryMapper the category mapper
     * @param productMapper the product mapper
     * @param pictureService the picture service
     * @param cache the cache for categories
     */
    public CategoryService(CategoryRepository categoryRepository,
                           CategoryMapper categoryMapper,
                           ProductMapper productMapper,
                           PictureService pictureService,
                           @Qualifier("categoryCache") GenericCache<UUID, Category> cache) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
        this.pictureService = pictureService;
        this.cache = cache;
    }

    /**
     * Retrieves a paginated list of categories with their products.
     *
     * @param page the page number
     * @param size the number of items per page
     * @return a page of CategoryProductsDto
     */
    public Page<CategoryProductsDto> getCategories(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return categoryRepository.findAllWithProducts(pageRequest)
                .map(category ->
                        categoryMapper.mapToCategoryProductsDto(category,
                                pictureService.getPictureUrl(category.getLogo()), getRelatedProductDtoList(category)));
    }

    /**
     * Retrieves a list of related product DTOs for a given category.
     *
     * @param category the category
     * @return a list of ProductDto
     */
    private List<ProductDto> getRelatedProductDtoList(Category category) {
        return category.getProducts().stream()
                .map(product -> productMapper.mapToProductDto(product, pictureService.getPictureUrl(product.getLogo())))
                .toList();
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id the category ID
     * @return the CategoryProductsDto
     */
    public CategoryProductsDto getCategoryById(UUID id) {
        Category category = this.cache.get(id).orElseGet(() -> getCategoryFromRepository(id));
        String logoUrl = pictureService.getPictureUrl(category.getLogo());

        return categoryMapper.mapToCategoryProductsDto(category, logoUrl, getRelatedProductDtoList(category));
    }

    /**
     * Adds a new category.
     *
     * @param newCategory the new category DTO
     * @param logo the category logo
     * @return the added CategoryDto
     */
    public CategoryDto addCategory(CategoryDto newCategory, MultipartFile logo) {
        validateCategoryUniqueness(newCategory.getName());

        Category category = createCategoryFromDto(newCategory, logo);

        Category savedCategory = categoryRepository.save(category);
        cache.put(savedCategory.getId(), category);

        return categoryMapper.mapToCategoryDto(savedCategory);
    }

    /**
     * Creates a category from a DTO and logo.
     *
     * @param newCategory the new category DTO
     * @param logo the category logo
     * @return the created Category
     */
    private Category createCategoryFromDto(CategoryDto newCategory, MultipartFile logo) {
        Category category = new Category();
        String categoryLogoKey = pictureService.uploadAndGetKey(logo);
        newCategory.setLogo(categoryLogoKey);
        categoryMapper.mapCategoryDtoToCategory(category, newCategory);

        return category;
    }

    /**
     * Validates the uniqueness of a category name.
     *
     * @param categoryName the category name
     * @throws DuplicatedCategoryException if the category name already exists
     */
    private void validateCategoryUniqueness(String categoryName) {
        Category existingCategory = categoryRepository.findByName(categoryName);
        if (existingCategory != null) {
            throw new DuplicatedCategoryException(categoryName);
        }
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id the category ID
     * @return a message indicating the category has been deleted
     */
    public String deleteCategory(UUID id) {
        Category category = cache.get(id).orElseGet(() -> getCategoryFromRepository(id));

        removeLogosFromStorage(category);
        categoryRepository.delete(category);
        cache.remove(id);

        return "Category with id " + id + " has been deleted";
    }

    /**
     * Removes logos from storage for a given category.
     *
     * @param category the category
     */
    private void removeLogosFromStorage(Category category) {
        List<String> logoUrls = category.getProducts().stream().map(Product::getLogo).toList();
        pictureService.deleteFilesFromS3(logoUrls);
        pictureService.deleteFileFromS3(category.getLogo());
    }

    /**
     * Retrieves a category from the repository by its ID.
     *
     * @param id the category ID
     * @return the Category
     * @throws CategoryNotFoundException if the category is not found
     */
    public Category getCategoryFromRepository(UUID id) {
        logger.info("Fetching category with id {} from repository", id);
        Category category = categoryRepository
                .findByIdWithProducts(id)
                .orElseThrow(() -> new CategoryNotFoundException(id.toString()));
        this.cache.put(id, category);

        return category;
    }
}
