package com.example.store.order.application

import com.example.store.category.domain.Category
import com.example.store.order.domain.Order
import com.example.store.order.domain.OrderDetails
import com.example.store.order.domain.OrderRepository
import com.example.store.product.domain.Product
import com.example.store.product.domain.ProductRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.assertEquals

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

    @BeforeEach
    fun setUp() {

    }

    @Test
    fun saveOrderWhenProductIdNoExistShouldReturnThrowIllegalArgumentException() {

        //These are the data that should come from the controller
        val order = Order.builder()
            .orderDetails(listOf(
                OrderDetails.builder().product(Product.builder().id(1).build()).quantity(10).build(),
                OrderDetails.builder().product(Product.builder().id(2).build()).quantity(15).build()))
            .build()

        Mockito.`when`(productRepository.findAllById(listOf(1L,2L))).thenReturn(listOf(
            Product(1, "Product 1", "Description 1", 10.0, 10, Category(1, "Category 1"), null),
            ))

        val exception = assertThrows<IllegalArgumentException> {
            orderService.save(order)
        }
        assertEquals("Product no encontrado", exception.message)

    }
}