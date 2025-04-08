package com.example.store.order.application;

import com.example.store.order.domain.*;
import com.example.store.product.domain.Product;
import com.example.store.product.domain.ProductRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderDetailsRepository orderDetailsRepository,
                            @Qualifier("postgresProductRepository") ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order save(Order order) {

        double total = 0D;

        for (OrderDetails orderDetails : order.getOrderDetails()) {

            Product product = productRepository.findById(orderDetails.getProduct().getId()).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
            orderDetails.setProduct(product);
            total += product.getPrice() * orderDetails.getQuantity();

            if(product.getStock() < orderDetails.getQuantity()) {
                throw new IllegalStateException("Stock insuficiente");
            }

            product.setStock(product.getStock() - orderDetails.getQuantity());
            orderDetails.setPrice(product.getPrice());
//            orderDetails.setOrder(order);

            productRepository.save(product);
        }

        order.setTotal(total);
        order.setStatus("PENDING");

        return orderRepository.save(order);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order no encontrado"));
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> findByUserId(UUID userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public void delete(Long id) {
        orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order no encontrado"));
        orderDetailsRepository.deleteByOrderId(id);
        orderRepository.deleteById(id);
    }

    @Transactional
    public Order addOrderDetails(Long orderId, List<OrderDetails> orderDetailsList) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order no encontrado"));
        orderDetailsList.forEach(orderDetails -> {
                Product product = productRepository.findById(orderDetails.getProduct().getId()).orElseThrow(() -> new IllegalArgumentException("Product no encontrado"));
                orderDetails.setProduct(product);
                orderDetails.setPrice(product.getPrice());
//                orderDetails.setOrder(order);
                order.setTotal(order.getTotal() + orderDetails.getQuantity()*product.getPrice());
                orderDetailsRepository.save(orderDetails);
            });
        return orderRepository.save(order); //orderDetailsRepository.save(orderDetails);
    }
}