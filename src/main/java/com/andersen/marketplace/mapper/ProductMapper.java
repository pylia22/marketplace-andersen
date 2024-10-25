package com.andersen.marketplace.mapper;

import com.andersen.marketplace.dto.ProductDto;
import com.andersen.marketplace.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "category", source = "product.category.name")
    ProductDto mapToProductDto(Product product);

    @Mapping(target = "category", ignore = true)
    void updateProductFromDto(@MappingTarget Product product, ProductDto updatedProduct);

    @Mapping(target = "id", source = "product.id")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "category", source = "product.category.name")
    @Mapping(target = "logo", source = "logoUrl")
    ProductDto mapToProductDto(Product product, String logoUrl);
}