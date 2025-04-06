package com.example.store.order.infrastructure.mapper;

import com.example.store.order.domain.Order;
import com.example.store.order.domain.OrderDetails;
import com.example.store.order.infrastructure.dto.*;
import com.example.store.order.infrastructure.entity.OrderDetailsEntity;
import com.example.store.order.infrastructure.entity.OrderEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    Order OrderEntityToOrder(OrderEntity entity);

    OrderEntity OrderToOrderEntity(Order order);

    OrderDetails OrderDetailsEntityToOrderDetails(OrderDetailsEntity entity);

    OrderDetailsEntity OrderDetailsToOrderDetailsEntity(OrderDetails orderDetails);

    Order OrderDTOToOrder(OrderDTO orderDTO);

    OrderResponseDTO OrderToOrderResponseDTO(Order order);

    @Mapping(source = "productId", target = "product.id")
    OrderDetails orderdetailsDTOToOrderDetails(OrderDetailsDTO orderDetailsDTO);

    @Mapping(source = "product.name",target = "productName")
    OrderDetailsResponseDTO OrderDetailsToOrderDetailsResponseDTO(OrderDetails orderDetails);

    @Mapping(source = "user.email", target = "userEmail")
    OrderResponseAdminDTO OrderToOrderResponseAdminDTO(Order order);

}
