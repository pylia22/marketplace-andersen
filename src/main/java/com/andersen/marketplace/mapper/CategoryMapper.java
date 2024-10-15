package com.andersen.marketplace.mapper;

import com.andersen.marketplace.dto.CategoryDto;
import com.andersen.marketplace.dto.ProductDto;
import com.andersen.marketplace.entity.Category;
import com.andersen.marketplace.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "name", source = "category.name")
    @Mapping(target = "logo", source = "category.logo")
    @Mapping(target = "products", source = "products")
    CategoryDto mapToCategoryDto(Category category, List<ProductDto> products);

    @Mapping(target = "products", ignore = true)
    void mapCategoryDtoToCategory(@MappingTarget Category category, CategoryDto newCategory);

    @Mapping(target = "products", source = "products")
    void mapCategoryDtoToCategory(@MappingTarget Category category, CategoryDto newCategory, List<Product> products);
}
