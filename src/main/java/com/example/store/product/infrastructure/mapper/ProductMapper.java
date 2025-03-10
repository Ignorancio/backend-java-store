package com.example.store.product.infrastructure.mapper;

import com.example.store.category.domain.Category;
import com.example.store.product.domain.Product;
import com.example.store.product.infrastructure.dto.ProductDTO;
import com.example.store.product.infrastructure.entity.ProductEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    Product productEntityToProduct(ProductEntity productEntity);


    ProductEntity productToProductEntity(Product productEntity);

    @Mapping(source = "category", target = "category", qualifiedByName = "stringToCategory")
    Product productDTOToProduct(ProductDTO productDTO);

    @Named("stringToCategory")
    default Category stringToCategory(String categoryName) {
        if (categoryName == null) {
            return null;
        }
        return Category.builder()
                .name(categoryName)
                .build();
    }

    @AfterMapping
    default void updateProductImage(@MappingTarget ProductEntity productEntity){
        if(productEntity.getProductImage() != null){
            productEntity.getProductImage().setProduct(productEntity);
        }
    }
}
