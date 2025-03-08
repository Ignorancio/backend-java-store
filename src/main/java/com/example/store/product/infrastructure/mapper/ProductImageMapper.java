package com.example.store.product.infrastructure.mapper;

import com.example.store.product.domain.ProductImage;
import com.example.store.product.infrastructure.entity.ProductImageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductImageMapper {

    ProductImage productImageEntityToProductImage(ProductImageEntity productImageEntity);

    ProductImageEntity productImageToProductImageEntity(ProductImage productImage);
}
