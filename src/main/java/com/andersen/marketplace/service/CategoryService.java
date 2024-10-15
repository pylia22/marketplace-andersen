package com.andersen.marketplace.service;

import com.andersen.marketplace.cache.GenericCache;
import com.andersen.marketplace.dto.CategoryDto;
import com.andersen.marketplace.entity.Category;
import com.andersen.marketplace.entity.Product;
import com.andersen.marketplace.mapper.CategoryMapper;
import com.andersen.marketplace.mapper.ProductMapper;
import com.andersen.marketplace.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;
    private final PictureService pictureService;
    private final GenericCache<Long, Category> cache;

    public CategoryService(CategoryRepository categoryRepository,
                           CategoryMapper categoryMapper,
                           ProductMapper productMapper,
                           PictureService pictureService, GenericCache<Long, Category> cache) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
        this.pictureService = pictureService;
        this.cache = cache;
    }

    public Page<CategoryDto> getCategories(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return categoryRepository.findAll(pageRequest).map(category ->
                categoryMapper.mapToCategoryDto(category, productMapper.mapToProductDtoList(category.getProducts())));
    }

    public String addCategory(CategoryDto newCategory, MultipartFile logo) {
        Category existingCategory = categoryRepository.findByName(newCategory.getName());
        if (existingCategory != null) {
            throw new RuntimeException("Category already exists: " + newCategory.getName());
        }
        Category category = new Category();
//        else {
//            List<Product> products = productMapper.mapProductListFromProductDtoList(newCategory.getProducts());
//            categoryMapper.mapCategoryDtoToCategory(category, newCategory, products);
//        }
        String categoryLogoKey = pictureService.uploadAndGetKey(logo);
        newCategory.setLogo(categoryLogoKey);
        categoryMapper.mapCategoryDtoToCategory(category, newCategory);

        categoryRepository.save(category);
        cache.put(category.getId(), category);

        return "New category has been added";
    }

    public String deleteCategory(Long id) {
        Category category = cache.get(id).orElseGet(() -> getCategoryFromRepository(id));

        removeLogosFromStorage(category);
        categoryRepository.delete(category);
        cache.remove(id);

        return "category has been deleted";
    }

    private void removeLogosFromStorage(Category category) {
        List<String> logoUrls = category.getProducts().stream().map(Product::getLogo).toList();
        pictureService.deleteFilesFromS3(logoUrls);
        pictureService.deleteFileFromS3(category.getLogo());
    }

    public CategoryDto getCategory(Long id) {
        Category category = this.cache.get(id).orElseGet(() -> this.getCategoryFromRepository(id));

        return categoryMapper.mapToCategoryDto(category, productMapper.mapToProductDtoList(category.getProducts()));
    }

    public Category getCategoryFromRepository(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("category not found"));
        this.cache.put(id, category);

        return category;
    }
}
