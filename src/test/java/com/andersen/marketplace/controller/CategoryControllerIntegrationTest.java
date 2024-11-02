package com.andersen.marketplace.controller;

import com.andersen.marketplace.cache.GenericCache;
import com.andersen.marketplace.config.IntegrationTestConfig;
import com.andersen.marketplace.dto.CategoryDto;
import com.andersen.marketplace.entity.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static com.andersen.marketplace.utils.TestConstants.TEST_CATEGORY_ID;
import static com.andersen.marketplace.utils.TestConstants.TEST_CATEGORY_NAME;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerIntegrationTest extends IntegrationTestConfig {

    @Qualifier("categoryCache")
    @Autowired
    private GenericCache<UUID, Product> cache;

    @AfterEach
    public void tearDown() {
        cache.clear();
    }

    @Test
    @Sql("/sql/add-category.sql")
    void shouldReturnCategoriesWhenCategoriesWereFound() throws Exception {
        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].name").value(TEST_CATEGORY_NAME));
    }

    @Test
    void shouldReturnCategoryWhenCategoryWasAdded() throws Exception {
        String newCategory = "new category";
        MockPart file = new MockPart("file", "fileName", "fileContent".getBytes());
        CategoryDto categoryDto = new CategoryDto(newCategory, null);
        MockPart contentPart = new MockPart("content", objectMapper.writeValueAsString(categoryDto).getBytes());
        contentPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(multipart(HttpMethod.POST, "/api/categories")
                        .part(file, contentPart))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(newCategory));
    }

    @Test
    @Sql("/sql/add-category.sql")
    void shouldReturnSuccessMessageWhenCategoryDeleted() throws Exception {
        mockMvc.perform(delete("/api/categories/" + TEST_CATEGORY_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Category with id " + TEST_CATEGORY_ID + " has been deleted"));
    }

    @Test
    @Sql("/sql/add-category.sql")
    void shouldReturnCategoryByIdWhenCategoryFound() throws Exception {
        mockMvc.perform(get("/api/categories/" + TEST_CATEGORY_ID))
                .andExpect(jsonPath("$.name").value(TEST_CATEGORY_NAME));
    }
}