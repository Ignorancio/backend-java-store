package com.example.store.product.application;

import com.example.store.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductUtils {
    void copyNonNullProperties(Product source, Product target){
        if ( source == null ) {
            return;
        }

        if ( source.getId() != null ) {
            target.setId( source.getId() );
        }
        if ( source.getName() != null ) {
            target.setName( source.getName() );
        }
        if ( source.getDescription() != null ) {
            target.setDescription( source.getDescription() );
        }
        if ( source.getPrice() != null ) {
            target.setPrice( source.getPrice() );
        }
        if ( source.getStock() != null ) {
            target.setStock( source.getStock() );
        }
        if ( source.getCategory() != null ) {
            target.setCategory( source.getCategory() );
        }
        if ( source.getProductImage() != null ) {
            target.setProductImage( source.getProductImage() );
        }
    }
}
