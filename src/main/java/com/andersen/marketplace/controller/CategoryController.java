package com.andersen.marketplace.controller;

import com.andersen.marketplace.dto.CategoryDto;
import com.andersen.marketplace.dto.CategoryProductsDto;
import com.andersen.marketplace.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("api/categories")
@Tag(name = "Category", description = "The Category API")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Get all categories", description = "Retrieve a paginated list of categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved categories"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<CategoryProductsDto>> getCategories(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(categoryService.getCategories(page, size));
    }

    @Operation(summary = "Add a new category", description = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added category",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@RequestPart(value = "content") CategoryDto categoryDto,
                                                   @RequestPart(value = "file") MultipartFile logo) {
        return ResponseEntity.ok(categoryService.addCategory(categoryDto, logo));
    }

    @Operation(summary = "Delete a category", description = "Delete a category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted category"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

    @Operation(summary = "Get a category by ID", description = "Retrieve a category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved category",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryProductsDto.class))),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("{id}")
    public ResponseEntity<CategoryProductsDto> getCategory(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

}
