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

    @AfterMapping
    default void updateOrderDetails(@MappingTarget OrderEntity orderEntity){
        if(orderEntity.getOrderDetails() != null){
            orderEntity.getOrderDetails().forEach(orderDetailsEntity -> orderDetailsEntity.setOrder(orderEntity));
        }
    }

    OrderDetails OrderDetailsEntityToOrderDetails(OrderDetailsEntity entity);

    OrderDetailsEntity OrderDetailsToOrderDetailsEntity(OrderDetails orderDetails);

    Order OrderDTOToOrder(OrderDTO orderDTO);

    @Mapping(source = "productId", target = "product.id")
    OrderDetails orderdetailsDTOToOrderDetails(OrderDetailsDTO orderDetailsDTO);
}
