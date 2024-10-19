package com.andersen.marketplace.service;

import com.andersen.marketplace.cache.GenericCache;
import com.andersen.marketplace.dto.ProductDto;
import com.andersen.marketplace.dto.ProductSearchRequest;
import com.andersen.marketplace.entity.Category;
import com.andersen.marketplace.entity.Product;
import com.andersen.marketplace.exception.CategoryBadRequestException;
import com.andersen.marketplace.exception.ProductNotFoundException;
import com.andersen.marketplace.mapper.ProductMapper;
import com.andersen.marketplace.repository.CategoryRepository;
import com.andersen.marketplace.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final PictureService pictureService;
    private final GenericCache<UUID, Product> cache;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository,
                          ProductMapper productMapper, PictureService pictureService,
                          GenericCache<UUID, Product> cache) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
        this.pictureService = pictureService;
        this.cache = cache;
    }

    public Page<ProductDto> getProducts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return productRepository.findAll(pageRequest).map(productMapper::mapToProductDto);
    }

    public Set<String> getUniqueProducts() {
        return productRepository.findUniqueProducts();
    }

    public Page<ProductDto> getFilteredProducts(ProductSearchRequest search, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return productRepository.findAllWithFilter(search.getProductCategory(), search.getProductName(), pageRequest)
                .map(productMapper::mapToProductDto);
    }

    public ProductDto editProduct(UUID id, ProductDto updatedProduct, MultipartFile logo) {
        Product product = this.cache.get(id).orElseGet(() -> {
            logger.info("Fetching product with id {} from repository", id);
            return productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException(id));
        });
        updateProductLogo(updatedProduct, logo, product.getLogo());
        productMapper.updateProductFromDto(product, updatedProduct);

        Product savedProduct = productRepository.save(product);
        cache.put(id, savedProduct);

        return productMapper.mapToProductDto(savedProduct);
    }

    private void updateProductLogo(ProductDto updatedProduct, MultipartFile newLogo, String currentLogo) {
        if (newLogo == null || newLogo.isEmpty()) {
            updatedProduct.setLogo(currentLogo);
        } else {
            updatedProduct.setLogo(pictureService.uploadAndGetKey(newLogo));
            pictureService.deleteFileFromS3(currentLogo);
        }
    }

    public ProductDto addProduct(ProductDto newProduct, MultipartFile logo) {
        Category category = getCategoryByName(newProduct.getCategory());
        String productLogoKey = pictureService.uploadAndGetKey(logo);
        Product product = new Product(newProduct.getName(), productLogoKey, category);

        Product savedProduct = productRepository.save(product);
        cache.put(savedProduct.getId(), savedProduct);

        return productMapper.mapToProductDto(savedProduct);
    }

    private Category getCategoryByName(String categoryName) {
        Category category = categoryRepository.findByName(categoryName);
        if (category == null) {
            throw new CategoryBadRequestException("Category " + categoryName + " not found");
        }
        return category;
    }

    public String deleteProduct(UUID id) {
        Product product = this.cache.get(id).orElseGet(() -> this.getProductFromRepository(id));

        pictureService.deleteFileFromS3(product.getLogo());
        productRepository.delete(product);
        cache.remove(id);

        return "product has been deleted";
    }

    public ProductDto getProduct(UUID id) {
        Product product = this.cache.get(id).orElseGet(() -> this.getProductFromRepository(id));

        return productMapper.mapToProductDto(product);
    }

    public Product getProductFromRepository(UUID id) {
        logger.info("Fetching product with id {} from repository", id);
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        this.cache.put(id, product);

        return product;
    }
}
