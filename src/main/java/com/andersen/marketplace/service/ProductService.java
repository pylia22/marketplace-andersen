package com.andersen.marketplace.service;

import com.andersen.marketplace.cache.GenericCache;
import com.andersen.marketplace.dto.ProductDto;
import com.andersen.marketplace.dto.ProductFilter;
import com.andersen.marketplace.entity.Category;
import com.andersen.marketplace.entity.Product;
import com.andersen.marketplace.mapper.ProductMapper;
import com.andersen.marketplace.repository.CategoryRepository;
import com.andersen.marketplace.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final PictureService pictureService;
    private final GenericCache<Long, Product> cache;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository,
                          ProductMapper productMapper, PictureService pictureService,
                          GenericCache<Long, Product> cache) {
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

    public Page<ProductDto> getFilteredProducts(ProductFilter filter, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return filter != null
                ? productRepository.findAllWithFilter(filter.getProductCategory(), filter.getProductName(), pageRequest)
                .map(productMapper::mapToProductDto)
                : productRepository.findAll(pageRequest)
                .map(productMapper::mapToProductDto);
    }

    public String editProduct(Long id, ProductDto updatedProduct, MultipartFile logo) {
        Product product = this.cache.get(id).orElseGet(() -> productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("product not found")));

        updateProductLogo(updatedProduct, logo, product.getLogo());

        productMapper.updateProductFromDto(product, updatedProduct);
        productRepository.save(product);
        cache.put(id, product);

        return "success message product updated";
    }

    private void updateProductLogo(ProductDto updatedProduct, MultipartFile newLogo, String currentLogo) {
        if (newLogo == null || newLogo.isEmpty()) {
            updatedProduct.setLogo(currentLogo);
        } else {
            updatedProduct.setLogo(pictureService.uploadAndGetKey(newLogo));
            pictureService.deleteFileFromS3(currentLogo);
        }
    }

    public String addProduct(ProductDto newProduct, MultipartFile logo) {
        Category category = categoryRepository.findByName(newProduct.getCategory());

        if (category == null) {
            throw new RuntimeException("Category not found: " + newProduct.getCategory());
        }
        String productLogoKey = pictureService.uploadAndGetKey(logo);
        Product product = new Product(newProduct.getName(), productLogoKey, category);

        productRepository.save(product);
        cache.put(product.getId(), product);

        return "new product has been added";
    }

    public String deleteProduct(Long id) {
        Product product = this.cache.get(id).orElseGet(() -> this.getProductFromRepository(id));

        pictureService.deleteFileFromS3(product.getLogo());
        productRepository.delete(product);
        cache.remove(id);

        return "product has been deleted";
    }

    public ProductDto getProduct(Long id) {
        Product product = this.cache.get(id).orElseGet(() -> this.getProductFromRepository(id));

        return productMapper.mapToProductDto(product);
    }

    public Product getProductFromRepository(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("product not found"));
        this.cache.put(id, product);

        return product;
    }
}
