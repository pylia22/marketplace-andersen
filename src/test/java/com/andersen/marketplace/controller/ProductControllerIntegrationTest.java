package com.andersen.marketplace.controller;

import com.andersen.marketplace.cache.GenericCache;
import com.andersen.marketplace.config.IntegrationTestConfig;
import com.andersen.marketplace.dto.ProductDto;
import com.andersen.marketplace.dto.ProductSearchRequest;
import com.andersen.marketplace.entity.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;
import java.util.UUID;

import static com.andersen.marketplace.utils.TestConstants.TEST_CATEGORY_NAME;
import static com.andersen.marketplace.utils.TestConstants.TEST_LOGO;
import static com.andersen.marketplace.utils.TestConstants.TEST_PRODUCT_ID;
import static com.andersen.marketplace.utils.TestConstants.TEST_PRODUCT_NAME;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerIntegrationTest extends IntegrationTestConfig {

    @Qualifier("productCache")
    @Autowired
    private GenericCache<UUID, Product> cache;

    @AfterEach
    public void tearDown() {
        cache.clear();
    }

    @Test
    @Sql("/sql/add-category.sql")
    void shouldReturnProductWhenProductWasAdded() throws Exception {
        MockPart file = new MockPart("file", "fileName", "fileContent".getBytes());
        ProductDto productDto = new ProductDto(TEST_PRODUCT_ID, TEST_PRODUCT_NAME, TEST_LOGO, TEST_CATEGORY_NAME);
        MockPart contentPart = new MockPart("content", objectMapper.writeValueAsString(productDto).getBytes());
        contentPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(multipart(HttpMethod.POST, "/api/products")
                        .part(file, contentPart))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(TEST_PRODUCT_NAME))
                .andExpect(jsonPath("$.category").value(TEST_CATEGORY_NAME));
    }

    @Test
    @Sql("/sql/add-products.sql")
    void shouldReturnProductByIdWhenProductFound() throws Exception {
        mockMvc.perform(get("/api/products/" + TEST_PRODUCT_ID))
                .andExpect(jsonPath("$.name").value(TEST_PRODUCT_NAME))
                .andExpect(jsonPath("$.category").value(TEST_CATEGORY_NAME));
    }

    @Test
    @Sql("/sql/add-products.sql")
    void shouldReturnProductsWhenProductsWereFound() throws Exception {
        mockMvc.perform(post("/api/products/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ProductSearchRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].name").value(TEST_PRODUCT_NAME))
                .andExpect(jsonPath("$.content[0].category").value(TEST_CATEGORY_NAME));
    }

    @Test
    @Sql("/sql/add-products.sql")
    void shouldReturnUniqueProductsWhenProductsWereFound() throws Exception {
        Set<String> uniqueProducts = Set.of("iPhone 16 Pro", "iPhone 15 Pro", "Asus VivoBook");

        mockMvc.perform(get("/api/products/unique")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", containsInAnyOrder(uniqueProducts.toArray())));
    }

    @Test
    @Sql("/sql/add-products.sql")
    void shouldEditProductWhenProductPresentById() throws Exception {
        String newProductName = "new product name";
        ProductDto productDto = new ProductDto(TEST_PRODUCT_ID, newProductName, TEST_LOGO, TEST_CATEGORY_NAME);
        MockPart contentPart = new MockPart("content", objectMapper.writeValueAsString(productDto).getBytes());
        contentPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/products/" + TEST_PRODUCT_ID)
                        .part(contentPart))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(newProductName))
                .andExpect(jsonPath("$.category").value(TEST_CATEGORY_NAME));
    }

    @Test
    @Sql("/sql/add-products.sql")
    void shouldReturnSuccessMessageWhenProductDeleted() throws Exception {
        mockMvc.perform(delete("/api/products/" + TEST_PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("product has been deleted"));
    }
}