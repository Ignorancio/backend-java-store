package com.example.store.order.application;

import com.example.store.order.domain.*;
import com.example.store.product.domain.Product;
import com.example.store.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderDetailsRepository orderDetailsRepository,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order save(Order order) {

        List<Long> productIds = order.getOrderDetails().stream()
                .map(orderDetails -> orderDetails.getProduct().getId())
                .toList();

        List<Product> products = productRepository.findAllById(productIds);
        HashMap<Long, Product> productMap = new HashMap<>();
        products.forEach(product -> productMap.put(product.getId(), product));

        double total = 0D;

        for (OrderDetails orderDetails : order.getOrderDetails()) {
            Product product = productMap.get(orderDetails.getProduct().getId());
            if (product == null) {
                throw new IllegalArgumentException("Product no encontrado");
            }
            if(product.getStock() < orderDetails.getQuantity()) {
                throw new IllegalStateException("Stock insuficiente");
            }
            orderDetails.setProduct(product);
            total += product.getPrice() * orderDetails.getQuantity();
            product.setStock(product.getStock() - orderDetails.getQuantity());
            orderDetails.setPrice(product.getPrice());

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
        orderRepository.deleteById(id);
    }

    @Transactional
    public Order addOrderDetails(Long orderId, List<OrderDetails> orderDetailsList) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order no encontrado"));
        if(!order.getStatus().equals("PENDING")) {
            throw new IllegalStateException("No se puede agregar mas productos a la orden");
        }
        orderDetailsList.forEach(orderDetails -> {
                Product product = productRepository.findById(orderDetails.getProduct().getId()).orElseThrow(() -> new IllegalArgumentException("Product no encontrado"));
                orderDetails.setProduct(product);
                orderDetails.setPrice(product.getPrice());
                order.setTotal(order.getTotal() + orderDetails.getQuantity()*product.getPrice());
                orderDetailsRepository.save(orderDetails);
            });
        return orderRepository.save(order);
    }
}