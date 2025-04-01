package com.example.store.product.infrastructure;

import com.example.store.auth.infrastructure.RegisterRequest;
import com.example.store.auth.infrastructure.TokenResponse;
import com.example.store.category.infrastructure.repository.QueryCategoryRepository;
import com.example.store.product.domain.Product;
import com.example.store.product.infrastructure.dto.ProductDTO;
import com.example.store.product.infrastructure.repository.CacheProductRepository;
import com.example.store.product.infrastructure.repository.QueryProductImageRepository;
import com.example.store.product.infrastructure.repository.QueryProductRepository;
import com.example.store.user.infrastructure.repository.QueryUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CacheProductRepository cacheProductRepository;
    @Autowired
    private QueryProductRepository productRepository;
    @Autowired
    private QueryProductImageRepository productImageRepository;
    @Autowired
    private QueryCategoryRepository categoryRepository;
    @Autowired
    private QueryUserRepository userRepository;

    private static String JWT = "";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    void setUpAll() throws Exception{

        cacheProductRepository.deleteAll();
        productImageRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        RegisterRequest authRequest = new RegisterRequest("admin@admin", "admin");

        String json = objectMapper.writeValueAsString(authRequest);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/auth/register/admin")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();

        TokenResponse response = objectMapper.readValue(jsonResponse, TokenResponse.class);

        JWT = response.accessToken();
    }

    @AfterEach
    void tearDown() {
        cacheProductRepository.deleteAll();
        productImageRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @AfterAll
    void tearDownAll() {
        userRepository.deleteAll();
    }

    @Test
    void save() throws Exception {

        ProductDTO productDTO = new ProductDTO("product 1", "description 1", 100.0, 100, "category 1");
        String productJson = objectMapper.writeValueAsString(productDTO);

        MockMultipartFile productFile = new MockMultipartFile("product","","application/json", productJson.getBytes());

        MockMultipartFile filePart = new MockMultipartFile("file", "product1.jpg", "image/jpeg", "image content".getBytes());

        MvcResult mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("http://localhost:8080/api/v1/products")
                        .file(productFile)
                        .file(filePart)
                        .header("Authorization", "Bearer " + JWT)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andReturn();

        String response = mockMvcResult.getResponse().getContentAsString();
        Product product = objectMapper.readValue(response, Product.class);

        assertNotNull(product.getId());
        assertNotNull(product.getCategory().getId());
        assertNotNull(product.getProductImage().getId());

        assertEquals("product 1", product.getName());
        assertEquals("description 1", product.getDescription());
        assertEquals(100.0, product.getPrice());
        assertEquals(100, product.getStock());
        assertEquals("category 1", product.getCategory().getName());
    }

    @Test
    @Disabled
    void findById() {
    }

    @Test
    void findAll() throws Exception {
        MvcResult mockMvcResult = mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, mockMvcResult.getResponse().getStatus());
    }

    @Test
    @Disabled
    void update() {
    }

    @Test
    @Disabled
    void delete() {
    }
}