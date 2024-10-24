package com.andersen.marketplace.service;

import com.andersen.marketplace.cache.GenericCache;
import com.andersen.marketplace.dto.ProductDto;
import com.andersen.marketplace.dto.ProductSearchRequest;
import com.andersen.marketplace.entity.Category;
import com.andersen.marketplace.entity.Product;
import com.andersen.marketplace.exception.CategoryNotFoundException;
import com.andersen.marketplace.exception.ProductNotFoundException;
import com.andersen.marketplace.mapper.ProductMapperImpl;
import com.andersen.marketplace.repository.CategoryRepository;
import com.andersen.marketplace.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.andersen.marketplace.utils.TestConstants.TEST_CATEGORY_ID;
import static com.andersen.marketplace.utils.TestConstants.TEST_CATEGORY_NAME;
import static com.andersen.marketplace.utils.TestConstants.TEST_LOGO;
import static com.andersen.marketplace.utils.TestConstants.TEST_PRODUCT_ID;
import static com.andersen.marketplace.utils.TestConstants.TEST_PRODUCT_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PictureService pictureService;

    @Mock
    private GenericCache<UUID, Product> cache;

    @Mock
    private MultipartFile file;

    @Spy
    private ProductMapperImpl productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldReturnProductPageWhenProductsExist() {
        Pageable pageable = PageRequest.of(0, 5);
        Product product = getProduct();
        ProductDto productDto = new ProductDto(product.getName(), product.getLogo(), product.getCategory().getName());
        List<ProductDto> expectedProducts = List.of(productDto);

        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(product)));

        List<ProductDto> actualProducts = productService.getProducts(0, 5).getContent();

        assertEquals(expectedProducts, actualProducts);
    }

    @Test
    void shouldReturnUniqueProductNamesWhenProductsExist() {
        Set<String> expectedUniqueProductNames = Set.of(TEST_PRODUCT_NAME);

        when(productRepository.findUniqueProducts()).thenReturn(expectedUniqueProductNames);

        assertEquals(expectedUniqueProductNames, productService.getUniqueProducts());
    }

    @Test
    void shouldReturnProductPageWhenFilterApplied() {
        ProductSearchRequest search = new ProductSearchRequest(TEST_CATEGORY_NAME, TEST_PRODUCT_NAME);
        Pageable pageable = PageRequest.of(0, 5);
        Product product = getProduct();
        ProductDto productDto = new ProductDto(product.getName(), product.getLogo(), product.getCategory().getName());
        List<ProductDto> expectedProducts = List.of(productDto);

        when(productRepository.findAllWithFilter(TEST_CATEGORY_NAME, TEST_PRODUCT_NAME, pageable))
                .thenReturn(new PageImpl<>(List.of(product)));

        List<ProductDto> actualProducts = productService.getFilteredProducts(search, 0, 5).getContent();

        assertEquals(expectedProducts, actualProducts);
    }

    @Test
    void shouldEditProductWhenProductExists() {
        Product product = getProduct();
        ProductDto updatedProduct = new ProductDto("updatedProductName", null, product.getCategory().getName());

        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        ProductDto actualProduct = productService.editProduct(TEST_PRODUCT_ID, updatedProduct, null);
        updatedProduct.setLogo(product.getLogo());

        assertEquals(updatedProduct, actualProduct);
    }

    @Test
    void shouldDeleteOldProductLogoWhenNewLogoUploaded() {
        Product product = getProduct();
        ProductDto updatedProduct = new ProductDto("updatedProductName", TEST_LOGO, product.getCategory().getName());

        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(file.isEmpty()).thenReturn(false);

        productService.editProduct(TEST_PRODUCT_ID, updatedProduct, file);

        verify(pictureService).uploadAndGetKey(file);
        verify(pictureService).deleteFileFromS3(TEST_LOGO);
    }

    @Test
    void shouldVerifySavedProductCached() {
        Product product = getProduct();
        Product savedProduct = getProduct();
        savedProduct.setId(TEST_PRODUCT_ID);
        Category category = product.getCategory();
        ProductDto newProduct = new ProductDto(TEST_PRODUCT_NAME, TEST_LOGO, category.getName());

        when(categoryRepository.findByName(TEST_CATEGORY_NAME)).thenReturn(category);
        when(pictureService.uploadAndGetKey(file)).thenReturn(TEST_LOGO);
        when(productRepository.save(product)).thenReturn(savedProduct);

        productService.addProduct(newProduct, file);

        verify(cache).put(TEST_PRODUCT_ID, savedProduct);
    }

    @Test
    void shouldThrowWhenCategoryNotFound() {
        Product product = getProduct();
        ProductDto newProduct = new ProductDto(TEST_PRODUCT_NAME, TEST_LOGO, product.getCategory().getName());

        when(categoryRepository.findByName(TEST_CATEGORY_NAME)).thenReturn(null);

        assertThrows(CategoryNotFoundException.class, () -> productService.addProduct(newProduct, file));
    }

    @Test
    void shouldReturnProductWhenProductFoundById() {
        Product product = getProduct();
        ProductDto expectedProduct = new ProductDto(TEST_PRODUCT_NAME, TEST_LOGO, TEST_CATEGORY_NAME);
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(product));

        ProductDto actualProduct = productService.getProductDto(TEST_PRODUCT_ID);

        assertEquals(expectedProduct, actualProduct);
        verify(cache).put(TEST_PRODUCT_ID, product);
    }

    @Test
    void shouldThrowWhenProductNotFoundById() {
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductDto(TEST_PRODUCT_ID));
    }

    @Test
    void shouldVerifyProductDeletedById() {
        Product product = getProduct();

        when(cache.get(TEST_PRODUCT_ID)).thenReturn(Optional.of(product));

        productService.deleteProduct(TEST_PRODUCT_ID);

        verify(pictureService).deleteFileFromS3(product.getLogo());
        verify(productRepository).delete(product);
        verify(cache).remove(TEST_PRODUCT_ID);
    }

    private Product getProduct() {
        Product product = new Product(TEST_PRODUCT_NAME, TEST_LOGO);
        Category category = new Category(TEST_CATEGORY_ID, TEST_CATEGORY_NAME, TEST_LOGO, List.of(product));
        product.setCategory(category);

        return product;
    }

}