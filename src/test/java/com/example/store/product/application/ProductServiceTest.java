package com.example.store.product.application;

import com.example.store.category.domain.CategoryRepository;
import com.example.store.product.domain.Product;
import com.example.store.product.domain.ProductRepository;
import com.example.store.product.domain.ProductService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository queryProductRepository;

    @Mock
    private ProductRepository cacheProductRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    public Product PRODUCT_PREPARED = Product.builder()
            .id(1L)
            .name("Product 1")
            .description("Description 1")
            .price(100.0)
            .stock(10)
            .category(null)
            .productImage(null)
            .build();
}
