package com.example.store.product.application;

import com.example.store.category.domain.Category;
import com.example.store.category.domain.CategoryRepository;
import com.example.store.product.domain.Product;
import com.example.store.product.domain.ProductImage;
import com.example.store.product.domain.ProductRepository;
import com.example.store.util.FileUpload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository queryProductRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private FileUpload fileUpload;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(
                queryProductRepository,
                null,
                categoryRepository,
                fileUpload,
                null
        );
    }

    @Test
    void save() {
        MultipartFile MULTIPART_FILE_PREPARED = new MockMultipartFile("test.jpg","test.jpg".getBytes(StandardCharsets.UTF_8));

        Product PRODUCT_BASE_PREPARED = Product.builder()
                .id(null)
                .name("Product 1")
                .description("Description 1")
                .price(100.0)
                .stock(10)
                .category(Category.builder().name("Category 1").build())
                .productImage(null)
                .build();

        Product PRODUCT_SAVED_PREPARED = Product.builder()
                .id(1L)
                .name("Product 1")
                .description("Description 1")
                .price(100.0)
                .stock(10)
                .category(Category.builder().id(1L).name("Category 1").build())
                .productImage(ProductImage.builder().id(1L).url("api/v1/products/images/test.jpg").build())
                .build();

        Mockito.when(categoryRepository.save(PRODUCT_BASE_PREPARED.getCategory())).thenReturn(Category.builder().id(1L).name(PRODUCT_BASE_PREPARED.getCategory().getName()).build());
        Mockito.when(fileUpload.uploadFile("/images",MULTIPART_FILE_PREPARED)).thenReturn("test.jpg");
        Mockito.when(queryProductRepository.save(PRODUCT_BASE_PREPARED)).thenReturn(PRODUCT_SAVED_PREPARED);

        Product product = productService.save(PRODUCT_BASE_PREPARED, MULTIPART_FILE_PREPARED);

        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals("Product 1", product.getName());
        assertEquals("Description 1", product.getDescription());
        assertEquals(100.0, product.getPrice());
        assertEquals(10, product.getStock());
        assertEquals(1L, product.getCategory().getId());
        assertEquals("api/v1/products/images/test.jpg", product.getProductImage().getUrl());
    }

    @Test
    void findAll(){
        Product PRODUCT_SAVED_PREPARED = Product.builder()
                .id(1L)
                .name("Product 1")
                .description("Description 1")
                .price(100.0)
                .stock(10)
                .category(Category.builder().id(1L).name("Category 1").build())
                .productImage(ProductImage.builder().id(1L).url("api/v1/products/images/test.jpg").build())
                .build();

        Mockito.when(queryProductRepository.findAll()).thenReturn(List.of(PRODUCT_SAVED_PREPARED));

        List<Product> products = productService.findAll();
        assertEquals(1, products.size());
        assertNotNull(products);
    }
}
