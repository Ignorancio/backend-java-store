package com.example.store.order.infrastructure

import com.example.store.auth.infrastructure.RegisterRequest
import com.example.store.auth.infrastructure.TokenResponse
import com.example.store.order.domain.Order
import com.example.store.order.infrastructure.dto.OrderDTO
import com.example.store.order.infrastructure.dto.OrderDetailsDTO
import com.example.store.order.infrastructure.repository.QueryOrderDetailsRepository
import com.example.store.order.infrastructure.repository.QueryOrderRepository
import com.example.store.product.domain.Product
import com.example.store.product.infrastructure.entity.ProductEntity
import com.example.store.product.infrastructure.mapper.ProductMapper
import com.example.store.product.infrastructure.repository.QueryProductRepository
import com.example.store.user.infrastructure.repository.QueryUserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderControllerImplTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val productRepository: QueryProductRepository,
    @Autowired private val orderRepository: QueryOrderRepository,
    @Autowired private val orderDetailsRepository: QueryOrderDetailsRepository,
    @Autowired private val userRepository: QueryUserRepository,
    @Autowired private val productMapper: ProductMapper
) {
    private var jwtUser: String = ""

    private val objectMapper = ObjectMapper()

    private lateinit var product1: Product
    private lateinit var product2: Product
    private lateinit var product3: Product

    @BeforeAll
    fun setUpAll(){

        orderDetailsRepository.deleteAll()
        orderRepository.deleteAll()
        productRepository.deleteAll()
        userRepository.deleteAll()

        val authRequest = RegisterRequest("user@user", "user")
        val json = objectMapper.writeValueAsString(authRequest)

        val mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                .contentType("application/json")
                .content(json))
            .andExpect(status().isOk)
            .andReturn()

        val jsonResponse = mvcResult.response.contentAsString
        val response = objectMapper.readValue(jsonResponse, TokenResponse::class.java)
        jwtUser = response.accessToken
    }

    @BeforeEach
    fun setup(){
        product1 = productMapper.productEntityToProduct(productRepository.save(ProductEntity.builder().name("product1").description("description1").price(10.0).stock(100).build()))
        product2 = productMapper.productEntityToProduct(productRepository.save(ProductEntity.builder().name("product2").description("description2").price(20.0).stock(200).build()))
        product3 = productMapper.productEntityToProduct(productRepository.save(ProductEntity.builder().name("product3").description("description3").price(30.0).stock(300).build()))
    }

    @AfterEach
    fun tearDown() {
        orderDetailsRepository.deleteAll()
        orderRepository.deleteAll()
        productRepository.deleteAll()
    }

    @AfterAll
    fun tearDownAll() {
        userRepository.deleteAll()
    }

    @Test
    fun save(){
        val orderDetailsDTO = listOf(
            OrderDetailsDTO(product1.id, 10),
            OrderDetailsDTO(product2.id, 15),
        )

        val orderDTO = OrderDTO(orderDetailsDTO)

        val orderJson = objectMapper.writeValueAsString(orderDTO)

        val mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders")
                    .contentType("application/json")
                    .content(orderJson)
                    .header("Authorization", "Bearer $jwtUser"))
            .andExpect(status().isCreated)
            .andReturn()

        val response = mockMvcResult.response.contentAsString
        val order = objectMapper.readValue(response, Order::class.java)

        assertNotNull(order.id)
        assertNotNull(order.user)

        assertEquals(400.0, order.total)
        assertEquals("PENDING", order.status)

        assertNotNull(order.orderDetails[0].id)
        assertNotNull(order.orderDetails[1].id)

        assertEquals(10, order.orderDetails[0].quantity)
        assertEquals(15, order.orderDetails[1].quantity)
        assertEquals(10.0, order.orderDetails[0].price)
        assertEquals(20.0, order.orderDetails[1].price)

    }
}