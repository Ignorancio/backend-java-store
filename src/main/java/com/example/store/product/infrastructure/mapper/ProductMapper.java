package com.example.store.product.infrastructure.mapper;

import com.example.store.product.domain.Product;
import com.example.store.product.infrastructure.dto.ProductDTO;
import com.example.store.product.infrastructure.entity.ProductCacheEntity;
import com.example.store.product.infrastructure.entity.ProductEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    Product productEntityToProduct(ProductEntity productEntity);


    ProductEntity productToProductEntity(Product productEntity);

    @Mapping(source = "category", target = "category.name")
    Product productDTOToProduct(ProductDTO productDTO);

    @AfterMapping
    default void updateProductImage(@MappingTarget ProductEntity productEntity){
        if(productEntity.getProductImage() != null){
            productEntity.getProductImage().setProduct(productEntity);
        }
    }

    ProductCacheEntity productToProductCacheEntity(Product product);

    Product productCacheEntityToProduct(ProductCacheEntity productCacheEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copyProperties(Product source,@MappingTarget Product target);
}
