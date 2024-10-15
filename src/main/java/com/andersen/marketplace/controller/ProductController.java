package com.andersen.marketplace.controller;

import com.andersen.marketplace.dto.ProductDto;
import com.andersen.marketplace.dto.ProductFilter;
import com.andersen.marketplace.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getProducts(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(productService.getProducts(page, size));
    }

    @GetMapping("unique")
    public ResponseEntity<Set<String>> getUniqueProducts() {
        return ResponseEntity.ok(productService.getUniqueProducts());
    }

    @PostMapping("search")
    public ResponseEntity<Page<ProductDto>> getFilteredProducts(@RequestBody(required = false) ProductFilter productFilter,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(productService.getFilteredProducts(productFilter, page, size));
    }

    @PutMapping("{id}")
    public ResponseEntity<String> editProduct(@PathVariable Long id,
                                              @RequestPart(value = "content") ProductDto productDto,
                                              @RequestPart(value = "file", required = false) MultipartFile logo) {
        return ResponseEntity.ok(productService.editProduct(id, productDto, logo));
    }

    @PostMapping
    public ResponseEntity<String> addProduct(@RequestPart(value = "content") ProductDto productDto,
                                             @RequestPart(value = "file") MultipartFile logo) {
        return ResponseEntity.ok(productService.addProduct(productDto, logo));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

}
