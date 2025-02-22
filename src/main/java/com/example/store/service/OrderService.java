package com.example.store.service;

import com.example.store.Utils.AuthUtil;
import com.example.store.dtos.OrderDetailsRequest;
import com.example.store.dtos.OrderDetailsResponse;
import com.example.store.dtos.OrderRequest;
import com.example.store.dtos.OrderResponse;
import com.example.store.model.Order;
import com.example.store.model.OrderDetails;
import com.example.store.model.Product;
import com.example.store.model.User;
import com.example.store.repository.OrderDetailsRepository;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import com.example.store.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest){
        String userEmail = AuthUtil.getAuthenticatedUserEmail();
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        List<OrderDetailsRequest> orderDetailsRequest = orderRequest.orderDetails();
        List<OrderDetailsResponse> orderDetailsResponse = new ArrayList<>();
        Order order = new Order();
        double total = calculateTotalAndCreateDetails(order,orderDetailsRequest,orderDetailsResponse);
        order.setUser(user);
        order.setTotal(total);
        order.setStatus("PENDING");
        orderRepository.save(order);
        return new OrderResponse(
                order.getId(),
                user.getEmail(),
                order.getTotal(),
                order.getStatus(),
                orderDetailsResponse
        );
    }
    @Transactional
    public String deleteOrder(Long id){
        Order order = orderRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Orden no encontrada"));
        orderDetailsRepository.deleteAll(order.getOrderDetails());
        orderRepository.delete(order);
        return "Orden eliminada";
    }
    public OrderResponse getOrder(Long id){
        String Email = AuthUtil.getAuthenticatedUserEmail();
        Order order = orderRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Orden no encontrada"));
        if(!order.getUser().getEmail().equals(Email)){
            throw new IllegalArgumentException("No tienes permiso para ver esta orden");
        }
        List<OrderDetails> orderDetails = order.getOrderDetails();
        List<OrderDetailsResponse> orderDetailsResponse = new ArrayList<>();
        for(OrderDetails orderDetail : orderDetails){
            orderDetailsResponse.add(new OrderDetailsResponse(
                    orderDetail.getId(),
                    orderDetail.getProduct().getName(),
                    orderDetail.getQuantity(),
                    orderDetail.getPrice()
            ));
        }
        return new OrderResponse(
                order.getId(),
                order.getUser().getEmail(),
                order.getTotal(),
                order.getStatus(),
                orderDetailsResponse
        );
    }
    public List<OrderResponse> getUserOrders(){
        String userEmail = AuthUtil.getAuthenticatedUserEmail();
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new IllegalArgumentException("Usuario no encontrado"));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderResponse> orderResponses = new ArrayList<>();
        for(Order order : orders){
            orderResponses.add(getOrder(order.getId()));
        }
        return orderResponses;
    }
    public List<OrderResponse> getAllOrders(){
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();
        for(Order order : orders){
            orderResponses.add(new OrderResponse(
                    order.getId(),
                    order.getUser().getEmail(),
                    order.getTotal(),
                    order.getStatus(),
                    new ArrayList<>()
            ));
        }
        return orderResponses;
    }
    private void validateStockAndPrice(Product product, OrderDetailsRequest orderDetailsRequest) {
        if(!(product.getStock() >= orderDetailsRequest.quantity())){
            throw new IllegalStateException("Stock insuficiente");
        }
        if(!(product.getPrice().equals(orderDetailsRequest.price()))){
            throw new IllegalStateException("Precio invalido");
        }
    }
    private void updateStock(Product product, OrderDetailsRequest orderDetailsRequest) {
        product.setStock(product.getStock() - orderDetailsRequest.quantity());
        productRepository.save(product);
    }
    private OrderDetailsResponse createOrderDetails(OrderDetailsRequest orderDetailsRequest, Order order) {
        Product product = productRepository.findById(orderDetailsRequest.productId()).orElseThrow(
                ()-> new IllegalArgumentException("Producto no encontrado"));
        OrderDetails orderDetails = OrderDetails.builder()
                .order(order)
                .product(product)
                .quantity(orderDetailsRequest.quantity())
                .price(orderDetailsRequest.price())
                .build();
        orderDetailsRepository.save(orderDetails);
        List<OrderDetails> orderDetailsList = order.getOrderDetails();
        orderDetailsList.add(orderDetails);
        order.setOrderDetails(orderDetailsList);
        return new OrderDetailsResponse(
                orderDetails.getId(),
                product.getName(),
                orderDetails.getQuantity(),
                orderDetails.getPrice()
        );
    }
    private Double calculateTotalAndCreateDetails(Order order, List<OrderDetailsRequest> orderDetailsRequest,List<OrderDetailsResponse> orderDetailsResponse) {
        double total = 0.0;
        for(OrderDetailsRequest orderDetails : orderDetailsRequest) {
            Product product = productRepository.findById(orderDetails.productId()).orElseThrow(()-> new IllegalArgumentException("Producto no encontrado"));
            validateStockAndPrice(product, orderDetails);
            orderDetailsResponse.add(createOrderDetails(orderDetails, order));
            total += orderDetails.quantity() * orderDetails.price();
            updateStock(product, orderDetails);
        }
        return total;
    }
}