package com.andersen.marketplace.controller;

import com.andersen.marketplace.dto.CategoryDto;
import com.andersen.marketplace.dto.CategoryProductsDto;
import com.andersen.marketplace.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<Page<CategoryProductsDto>> getCategories(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(categoryService.getCategories(page, size));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@RequestPart(value = "content") CategoryDto categoryDto,
                                                   @RequestPart(value = "file") MultipartFile logo) {
        return ResponseEntity.ok(categoryService.addCategory(categoryDto, logo));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryProductsDto> getCategory(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }
}
