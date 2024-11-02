package com.andersen.marketplace.controller;

import com.andersen.marketplace.dto.ProductDto;
import com.andersen.marketplace.dto.ProductSearchRequest;
import com.andersen.marketplace.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("api/products")
@Tag(name = "Product", description = "The Product API")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get all products", description = "Retrieve a paginated list of products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<ProductDto>> getProducts(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(productService.getProducts(page, size));
    }

    @Operation(summary = "Get unique products", description = "Retrieve unique product names")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved unique products"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("unique")
    public ResponseEntity<Set<String>> getUniqueProducts() {
        return ResponseEntity.ok(productService.getUniqueProducts());
    }

    @Operation(summary = "Search products", description = "Retrieve a paginated list of products based on search criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered products"),
            @ApiResponse(responseCode = "400", description = "Invalid search criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("search")
    public ResponseEntity<Page<ProductDto>> getFilteredProducts(@RequestBody ProductSearchRequest search,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(productService.getFilteredProducts(search, page, size));
    }

    @Operation(summary = "Edit a product", description = "Edit an existing product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully edited product",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("{id}")
    public ResponseEntity<ProductDto> editProduct(@PathVariable UUID id,
                                                  @RequestPart(value = "content") ProductDto productDto,
                                                  @RequestPart(value = "file", required = false) MultipartFile logo) {
        return ResponseEntity.ok(productService.editProduct(id, productDto, logo));
    }

    @Operation(summary = "Add a new product", description = "Create a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added product",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestPart(value = "content") ProductDto productDto,
                                                 @RequestPart(value = "file") MultipartFile logo) {
        return ResponseEntity.ok(productService.addProduct(productDto, logo));
    }

    @Operation(summary = "Delete a product", description = "Delete a product by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted product"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @Operation(summary = "Get a product by ID", description = "Retrieve a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved product",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductDto(id));
    }
}
