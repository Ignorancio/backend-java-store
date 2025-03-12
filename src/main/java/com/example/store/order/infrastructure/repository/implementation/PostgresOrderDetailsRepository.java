package com.example.store.order.infrastructure.repository.implementation;

import com.example.store.order.domain.OrderDetails;
import com.example.store.order.domain.OrderDetailsRepository;
import com.example.store.order.infrastructure.entity.OrderDetailsEntity;
import com.example.store.order.infrastructure.mapper.OrderMapper;
import com.example.store.order.infrastructure.repository.QueryOrderDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class PostgresOrderDetailsRepository implements OrderDetailsRepository {

    private final QueryOrderDetailsRepository queryOrderDetailsRepository;
    private final OrderMapper orderMapper;

    public OrderDetails save(OrderDetails orderDetails) {
        OrderDetailsEntity entity = queryOrderDetailsRepository.save(orderMapper.OrderDetailsToOrderDetailsEntity(orderDetails));
        return orderMapper.OrderDetailsEntityToOrderDetails(entity);
    }

    public void deleteByOrderId(Long orderId) {
        queryOrderDetailsRepository.deleteByOrderId(orderId);
    }
}
