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
    private lateinit var jwtUser: String
    private lateinit var jwtUser2: String
    private lateinit var jwtAdmin: String

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


        val authRequest2 = RegisterRequest("user2@user", "user2")
        val json2 = objectMapper.writeValueAsString(authRequest2)

        val mvcResult2 = mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
            .contentType("application/json")
            .content(json2))
            .andExpect(status().isOk)
            .andReturn()

        val jsonResponse2 = mvcResult2.response.contentAsString
        val response2 = objectMapper.readValue(jsonResponse2, TokenResponse::class.java)
        jwtUser2 = response2.accessToken

        val authRequestAdmin = RegisterRequest("admin@admin", "admin")
        val jsonAdmin = objectMapper.writeValueAsString(authRequestAdmin)

        val mvcResultAdmin = mockMvc.perform(MockMvcRequestBuilders.post("/auth/register/admin")
            .contentType("application/json")
            .content(jsonAdmin))
            .andExpect(status().isOk)
            .andReturn()

        val jsonResponseAdmin = mvcResultAdmin.response.contentAsString
        val responseAdmin = objectMapper.readValue(jsonResponseAdmin, TokenResponse::class.java)
        jwtAdmin = responseAdmin.accessToken
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

    @Test
    fun findByOrderIdWhenUserIsCreatorShouldReturnOrder(){
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

        val mockMvcFind = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/orders/${order.id}")
                    .header("Authorization", "Bearer $jwtUser"))
            .andExpect(status().isOk)
            .andReturn()

        val jsonResponse = mockMvcFind.response.contentAsString
        val orderFind = objectMapper.readValue(jsonResponse, Order::class.java)

        assertEquals(order.id, orderFind.id)
        assertEquals(order.user, orderFind.user)
        assertEquals(order.total, orderFind.total)
        assertEquals(order.status, orderFind.status)

        assertEquals(order.orderDetails[0].id, orderFind.orderDetails[0].id)
        assertEquals(order.orderDetails[0].quantity, orderFind.orderDetails[0].quantity)
        assertEquals(order.orderDetails[0].price, orderFind.orderDetails[0].price)

        assertEquals(order.orderDetails[1].id, orderFind.orderDetails[1].id)
        assertEquals(order.orderDetails[1].quantity, orderFind.orderDetails[1].quantity)
        assertEquals(order.orderDetails[1].price, orderFind.orderDetails[1].price)

        assertEquals(product1.id, orderFind.orderDetails[0].product.id)
        assertEquals(90, orderFind.orderDetails[0].product.stock)
        assertEquals(product2.id, orderFind.orderDetails[1].product.id)
        assertEquals(185, orderFind.orderDetails[1].product.stock)
    }

    @Test
    fun findByOrderIdWhenUserIsNotCreatorShouldReturnForbidden(){
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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/orders/${order.id}")
            .header("Authorization", "Bearer $jwtUser2"))
            .andExpect(status().isForbidden)
    }

    @Test
    fun findByOrderIdWhenUserIsNotCreatorButIsAdminShouldReturnOrder() {
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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/orders/${order.id}")
            .header("Authorization", "Bearer $jwtAdmin"))
            .andExpect(status().isOk)
    }

    @Test
    fun findByUserIdShouldReturnOrdersAssociatedWithUserId() {
        val orderDetailsDTO = listOf(
            OrderDetailsDTO(product1.id, 10),
            OrderDetailsDTO(product2.id, 15),
        )

        val orderDTO = OrderDTO(orderDetailsDTO)

        val orderJson = objectMapper.writeValueAsString(orderDTO)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders")
                    .contentType("application/json")
                    .content(orderJson)
                    .header("Authorization", "Bearer $jwtUser"))
            .andExpect(status().isCreated)
            .andReturn()

        val mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/orders")
                    .header("Authorization", "Bearer $jwtUser"))
            .andExpect(status().isOk)
            .andReturn()

        val response = mvcResult.response.contentAsString
        val order: List<Order> = objectMapper.readerForListOf(Order::class.java).readValue(response)

        assertEquals(1, order.size)
    }

    @Test
    fun findAllWhenUserIsAdminReturnAllOrders(){
        val orderDetailsDTO = listOf(
            OrderDetailsDTO(product1.id, 10),
            OrderDetailsDTO(product2.id, 15),
        )

        val orderDTO = OrderDTO(orderDetailsDTO)

        val orderJson = objectMapper.writeValueAsString(orderDTO)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders")
                    .contentType("application/json")
                    .content(orderJson)
                    .header("Authorization", "Bearer $jwtUser"))
            .andExpect(status().isCreated)
            .andReturn()

        val mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/orders/all")
                    .header("Authorization", "Bearer $jwtAdmin"))
            .andExpect(status().isOk)
            .andReturn()

        val response = mvcResult.response.contentAsString
        val order: List<Order> = objectMapper.readerForListOf(Order::class.java).readValue(response)

        assertEquals(1, order.size)
    }
}