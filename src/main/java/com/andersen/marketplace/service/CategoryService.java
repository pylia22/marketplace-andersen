package com.andersen.marketplace.service;

import com.andersen.marketplace.cache.GenericCache;
import com.andersen.marketplace.dto.CategoryDto;
import com.andersen.marketplace.dto.CategoryProductsDto;
import com.andersen.marketplace.entity.Category;
import com.andersen.marketplace.entity.Product;
import com.andersen.marketplace.exception.CategoryBadRequestException;
import com.andersen.marketplace.mapper.CategoryMapper;
import com.andersen.marketplace.mapper.ProductMapper;
import com.andersen.marketplace.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;
    private final PictureService pictureService;
    private final GenericCache<UUID, Category> cache;

    public CategoryService(CategoryRepository categoryRepository,
                           CategoryMapper categoryMapper,
                           ProductMapper productMapper,
                           PictureService pictureService,
                           GenericCache<UUID, Category> cache) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
        this.pictureService = pictureService;
        this.cache = cache;
    }

    public Page<CategoryProductsDto> getCategories(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return categoryRepository.findAllWithProducts(pageRequest).map(category ->
                categoryMapper.mapToCategoryProductsDto(category, productMapper.mapToProductDtoList(category.getProducts())));
    }

    public CategoryDto addCategory(CategoryDto newCategory, MultipartFile logo) {
        Category existingCategory = categoryRepository.findByName(newCategory.getName());
        if (existingCategory != null) {
            throw new CategoryBadRequestException("Category already exists: " + newCategory.getName());
        }
        Category category = new Category();
        String categoryLogoKey = pictureService.uploadAndGetKey(logo);
        newCategory.setLogo(categoryLogoKey);
        categoryMapper.mapCategoryDtoToCategory(category, newCategory);

        Category savedCategory = categoryRepository.save(category);
        cache.put(savedCategory.getId(), category);

        return categoryMapper.mapToCategoryDto(savedCategory);
    }

    public String deleteCategory(UUID id) {
        Category category = cache.get(id).orElseGet(() -> getCategoryFromRepository(id));

        removeLogosFromStorage(category);
        categoryRepository.delete(category);
        cache.remove(id);

        return "Category with id " + id + " has been deleted";
    }

    private void removeLogosFromStorage(Category category) {
        List<String> logoUrls = category.getProducts().stream().map(Product::getLogo).toList();
        pictureService.deleteFilesFromS3(logoUrls);
        pictureService.deleteFileFromS3(category.getLogo());
    }

    public CategoryProductsDto getCategory(UUID id) {
        Category category = this.cache.get(id).orElseGet(() -> this.getCategoryFromRepository(id));

        return categoryMapper.mapToCategoryProductsDto(category, productMapper.mapToProductDtoList(category.getProducts()));
    }

    public Category getCategoryFromRepository(UUID id) {
        logger.info("Fetching category with id {} from repository", id);
        Category category = categoryRepository
                .findByIdWithProducts(id)
                .orElseThrow(() -> new CategoryBadRequestException("Category with id " + id + " not found"));
        this.cache.put(id, category);

        return category;
    }
}
