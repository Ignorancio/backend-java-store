package com.example.store.order.application;

import com.example.store.order.infrastructure.repository.OrderDetailsRepository;
import com.example.store.util.AuthUtil;
import com.example.store.order.infrastructure.dto.OrderDetailsRequest;
import com.example.store.order.infrastructure.dto.OrderDetailsResponse;
import com.example.store.order.infrastructure.dto.OrderRequest;
import com.example.store.order.infrastructure.dto.OrderResponse;
import com.example.store.order.infrastructure.entity.OrderEntity;
import com.example.store.order.infrastructure.entity.OrderDetailsEntity;
import com.example.store.product.infrastructure.entity.ProductEntity;
import com.example.store.user.infrastructure.entity.UserEntity;
import com.example.store.order.infrastructure.repository.OrderRepository;
import com.example.store.product.infrastructure.repository.QueryProductRepository;
import com.example.store.user.infrastructure.repository.QueryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl {

    private final QueryUserRepository userRepository;
    private final QueryProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest){
        String userEmail = AuthUtil.getAuthenticatedUserEmail();
        UserEntity user = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        List<OrderDetailsRequest> orderDetailsRequest = orderRequest.orderDetails();
        List<OrderDetailsResponse> orderDetailsResponse = new ArrayList<>();
        OrderEntity order = new OrderEntity();
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
        String Email = AuthUtil.getAuthenticatedUserEmail();
        boolean isAdmin = AuthUtil.hasRole("ADMIN");
        OrderEntity order = orderRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Orden no encontrada"));
        if(!isAdmin && !order.getUser().getEmail().equals(Email)){
            throw new IllegalArgumentException("No tienes permiso para eliminar esta orden");
        }
        orderDetailsRepository.deleteAll(order.getOrderDetails());
        orderRepository.delete(order);
        return "Orden eliminada";
    }

    public OrderResponse getOrder(Long id){
        String Email = AuthUtil.getAuthenticatedUserEmail();
        boolean isAdmin = AuthUtil.hasRole("ADMIN");
        OrderEntity order = orderRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Orden no encontrada"));
        if(!isAdmin && !order.getUser().getEmail().equals(Email)){
            throw new IllegalArgumentException("No tienes permiso para ver esta orden");
        }
        List<OrderDetailsEntity> orderDetails = order.getOrderDetails();
        List<OrderDetailsResponse> orderDetailsResponse = new ArrayList<>();
        for(OrderDetailsEntity orderDetail : orderDetails){
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
        UserEntity user = userRepository.findByEmail(userEmail).orElseThrow(()-> new IllegalArgumentException("Usuario no encontrado"));
        List<OrderEntity> orders = orderRepository.findByUser(user);
        List<OrderResponse> orderResponses = new ArrayList<>();
        for(OrderEntity order : orders){
            List<OrderDetailsResponse> orderDetailsResponse = new ArrayList<>();
            for(OrderDetailsEntity orderDetail : order.getOrderDetails()){
                orderDetailsResponse.add(new OrderDetailsResponse(
                        orderDetail.getId(),
                        orderDetail.getProduct().getName(),
                        orderDetail.getQuantity(),
                        orderDetail.getPrice()
                ));
            }
            orderResponses.add(new OrderResponse(
                    order.getId(),
                    userEmail,
                    order.getTotal(),
                    order.getStatus(),
                    orderDetailsResponse
            ));
        }
        return orderResponses;
    }

    public List<OrderResponse> getAllOrders(){
        List<OrderEntity> orders = orderRepository.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();
        for(OrderEntity order : orders){
            List<OrderDetailsResponse> orderDetailsResponse = new ArrayList<>();
            for(OrderDetailsEntity orderDetail : order.getOrderDetails()){
                orderDetailsResponse.add(new OrderDetailsResponse(
                        orderDetail.getId(),
                        orderDetail.getProduct().getName(),
                        orderDetail.getQuantity(),
                        orderDetail.getPrice()
                ));
            }
            orderResponses.add(new OrderResponse(
                    order.getId(),
                    order.getUser().getEmail(),
                    order.getTotal(),
                    order.getStatus(),
                    orderDetailsResponse
            ));
        }
        return orderResponses;
    }

    private void validateStockAndPrice(ProductEntity product, OrderDetailsRequest orderDetailsRequest) {
        if(!(product.getStock() >= orderDetailsRequest.quantity())){
            throw new IllegalStateException("Stock insuficiente");
        }
        if(!(product.getPrice().equals(orderDetailsRequest.price()))){
            throw new IllegalStateException("Precio invalido");
        }
    }

    private void updateStock(ProductEntity product, OrderDetailsRequest orderDetailsRequest) {
        product.setStock(product.getStock() - orderDetailsRequest.quantity());
        productRepository.save(product);
    }

    private OrderDetailsResponse createOrderDetails(OrderDetailsRequest orderDetailsRequest, OrderEntity order) {
        ProductEntity product = productRepository.findById(orderDetailsRequest.productId()).orElseThrow(
                ()-> new IllegalArgumentException("Producto no encontrado"));
        OrderDetailsEntity orderDetails = OrderDetailsEntity.builder()
                .order(order)
                .product(product)
                .quantity(orderDetailsRequest.quantity())
                .price(orderDetailsRequest.price())
                .build();
        orderDetailsRepository.save(orderDetails);
        List<OrderDetailsEntity> orderDetailsList = order.getOrderDetails();
        orderDetailsList.add(orderDetails);
        order.setOrderDetails(orderDetailsList);
        return new OrderDetailsResponse(
                orderDetails.getId(),
                product.getName(),
                orderDetails.getQuantity(),
                orderDetails.getPrice()
        );
    }

    private Double calculateTotalAndCreateDetails(OrderEntity order, List<OrderDetailsRequest> orderDetailsRequest, List<OrderDetailsResponse> orderDetailsResponse) {
        double total = 0.0;
        for(OrderDetailsRequest orderDetails : orderDetailsRequest) {
            ProductEntity product = productRepository.findById(orderDetails.productId()).orElseThrow(()-> new IllegalArgumentException("Producto no encontrado"));
            validateStockAndPrice(product, orderDetails);
            orderDetailsResponse.add(createOrderDetails(orderDetails, order));
            total += orderDetails.quantity() * orderDetails.price();
            updateStock(product, orderDetails);
        }
        return total;
    }
}