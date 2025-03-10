package com.example.store.order.infrastructure.mapper;

import com.example.store.order.domain.Order;
import com.example.store.order.domain.OrderDetails;
import com.example.store.order.infrastructure.entity.OrderDetailsEntity;
import com.example.store.order.infrastructure.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    Order OrderEntityToOrder(OrderEntity entity);

    OrderEntity OrderToOrderEntity(Order order);

    OrderDetails OrderDetailsEntityToOrderDetails(OrderDetailsEntity entity);

    OrderDetailsEntity OrderDetailsToOrderDetailsEntity(OrderDetails orderDetails);
}
