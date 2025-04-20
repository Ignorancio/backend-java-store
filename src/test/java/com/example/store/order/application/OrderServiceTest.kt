package com.example.store.order.application

import com.example.store.category.domain.Category
import com.example.store.order.domain.OrderRepository
import com.example.store.order.infrastructure.dto.OrderDTO
import com.example.store.order.infrastructure.dto.OrderDetailsDTO
import com.example.store.order.infrastructure.mapper.OrderMapper
import com.example.store.product.domain.Product
import com.example.store.product.domain.ProductRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class OrderServiceTest {

    @Mock
    private lateinit var orderRepository: OrderRepository
    @Mock
    private lateinit var productRepository: ProductRepository
    @Mock
    private lateinit var orderDetailsRepository: OrderRepository

    @InjectMocks
    private lateinit var orderService: OrderServiceImpl

    private lateinit var orderMapper: OrderMapper

    @BeforeEach
    fun setUp() {

    }

//    @Test
//    fun saveOrderShouldReturnOrder() {
//        // Given
//        val orderDetailsDTO = listOf(
//            OrderDetailsDTO(1, 10),
//            OrderDetailsDTO(2, 15),
//        )
//
//        val orderDTO = OrderDTO(orderDetailsDTO)
//
//        val order = orderMapper.OrderDTOToOrder(orderDTO)
//
//        Mockito.`when`(productRepository.findAllById(listOf(1L,2L))).thenReturn(listOf(
//            Product(1, "Product 1", "Description 1", 10.0, 10, Category(1, "Category 1"), null),
//            Product(2, "Product 2", "Description 2", 20.0, 15, Category(2, "Category 2"), null)))
//
//        Mockito.`when`(orderRepository.save(order)).thenReturn(order)
//        // When
//        orderService.save(order)
//
//        // Then
//        // Verify that the order and order details are saved correctly
//    }
}