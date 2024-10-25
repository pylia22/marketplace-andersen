package com.andersen.marketplace.mapper;

import com.andersen.marketplace.dto.CategoryDto;
import com.andersen.marketplace.dto.CategoryProductsDto;
import com.andersen.marketplace.dto.ProductDto;
import com.andersen.marketplace.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", source = "category.id")
    @Mapping(target = "name", source = "category.name")
    @Mapping(target = "logo", source = "logoUrl")
    @Mapping(target = "products", source = "products")
    CategoryProductsDto mapToCategoryProductsDto(Category category, String logoUrl, List<ProductDto> products);

    CategoryDto mapToCategoryDto(Category category);

    @Mapping(target = "products", ignore = true)
    void mapCategoryDtoToCategory(@MappingTarget Category category, CategoryDto newCategory);

}
