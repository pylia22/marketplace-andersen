package com.andersen.marketplace.service;

import com.andersen.marketplace.cache.GenericCache;
import com.andersen.marketplace.dto.CategoryDto;
import com.andersen.marketplace.dto.CategoryProductsDto;
import com.andersen.marketplace.dto.ProductDto;
import com.andersen.marketplace.entity.Category;
import com.andersen.marketplace.entity.Product;
import com.andersen.marketplace.exception.CategoryNotFoundException;
import com.andersen.marketplace.exception.DuplicatedCategoryException;
import com.andersen.marketplace.mapper.CategoryMapperImpl;
import com.andersen.marketplace.mapper.ProductMapperImpl;
import com.andersen.marketplace.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.andersen.marketplace.utils.TestConstants.TEST_CATEGORY_ID;
import static com.andersen.marketplace.utils.TestConstants.TEST_CATEGORY_NAME;
import static com.andersen.marketplace.utils.TestConstants.TEST_LOGO;
import static com.andersen.marketplace.utils.TestConstants.TEST_LOGO_KEY;
import static com.andersen.marketplace.utils.TestConstants.TEST_PRODUCT_ID;
import static com.andersen.marketplace.utils.TestConstants.TEST_PRODUCT_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Spy
    private CategoryMapperImpl categoryMapper;

    @Spy
    private ProductMapperImpl productMapper;

    @Mock
    private PictureService pictureService;

    @Mock
    private MultipartFile file;

    @Mock
    private GenericCache<UUID, Category> cache;

    @Captor
    private ArgumentCaptor<Category> categoryCaptor;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void shouldReturnCategoriesWithProductsWhenCategoriesExist() {
        Pageable pageable = PageRequest.of(0, 5);
        Category category = getCategory();
        Product product = new Product(TEST_PRODUCT_ID, TEST_PRODUCT_NAME, TEST_LOGO, category);
        category.setProducts(List.of(product));
        Page<Category> page = new PageImpl<>(List.of(category));

        ProductDto productDto = new ProductDto(product.getId(), product.getName(), product.getLogo(), category.getName());
        CategoryProductsDto expected = new CategoryProductsDto(category.getName(), category.getLogo(), List.of(productDto));

        when(categoryRepository.findAllWithProducts(pageable)).thenReturn(page);

        Page<CategoryProductsDto> actual = categoryService.getCategories(0, 5);

        assertEquals(expected, actual.getContent().get(0));
    }

    @Test
    void shouldSaveCategoryWhenCategoryNotFoundByName() {
        Category savedCategory = getCategory();

        when(categoryRepository.findByName(TEST_CATEGORY_NAME)).thenReturn(null);
        when(pictureService.uploadAndGetKey(any(MultipartFile.class))).thenReturn(TEST_LOGO_KEY);
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryDto result = categoryService.addCategory(new CategoryDto(TEST_CATEGORY_NAME, TEST_LOGO), file);

        assertEquals(TEST_CATEGORY_NAME, result.getName());
    }

    @Test
    void shouldThrowWhenCategoryFoundByName() {
        CategoryDto categoryDto = new CategoryDto(TEST_CATEGORY_NAME, TEST_LOGO);

        when(categoryRepository.findByName(TEST_CATEGORY_NAME)).thenReturn(new Category());

        assertThrows(DuplicatedCategoryException.class, () -> categoryService.addCategory(categoryDto, file));
    }

    @Test
    void shouldVerifyCategoryByIdWithRelatedLogosDeleted() {
        Category category = getCategory();
        Product product = new Product(TEST_PRODUCT_NAME, TEST_LOGO, category);
        category.setProducts(List.of(product));

        when(cache.get(TEST_CATEGORY_ID)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(TEST_CATEGORY_ID);

        verify(pictureService, times(1)).deleteFilesFromS3(List.of(product.getLogo()));
        verify(pictureService, times(1)).deleteFileFromS3(category.getLogo());
        verify(categoryRepository, times(1)).delete(categoryCaptor.capture());
        verify(cache, times(1)).get(TEST_CATEGORY_ID);

        assertEquals(category, categoryCaptor.getValue());
    }

    @Test
    void shouldReturnCategoryWithProductsWhenCategoryFoundById() {
        Category category = getCategory();
        CategoryProductsDto expectedCategoryProductsDto = new CategoryProductsDto(category.getName(), category.getLogo(), null);

        when(categoryRepository.findByIdWithProducts(TEST_CATEGORY_ID)).thenReturn(Optional.of(category));

        CategoryProductsDto actualCategoryProductsDto = categoryService.getCategory(TEST_CATEGORY_ID);

        assertEquals(expectedCategoryProductsDto, actualCategoryProductsDto);
        verify(cache).put(TEST_CATEGORY_ID, category);
    }

    @Test
    void shouldThrowWhenCategoryNotFoundById() {
        when(categoryRepository.findByIdWithProducts(TEST_CATEGORY_ID)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategory(TEST_CATEGORY_ID));
    }

    private Category getCategory() {
        return new Category(TEST_CATEGORY_ID, TEST_CATEGORY_NAME, TEST_LOGO, null);
    }
}