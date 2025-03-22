package com.example.store.search.infrastructure.mapper;

import com.example.store.product.domain.Product;
import com.example.store.search.infrastructure.entity.ProductElasticEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SearchMapper {

    Product ProductElasticEntityToProduct(ProductElasticEntity entity);

    ProductElasticEntity ProductToProductElasticEntity(Product product);
}
