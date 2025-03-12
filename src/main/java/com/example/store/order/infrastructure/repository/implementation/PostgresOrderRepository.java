package com.example.store.order.infrastructure.repository.implementation;

import com.example.store.order.domain.Order;
import com.example.store.order.domain.OrderRepository;
import com.example.store.order.infrastructure.entity.OrderEntity;
import com.example.store.order.infrastructure.mapper.OrderMapper;
import com.example.store.order.infrastructure.repository.QueryOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class PostgresOrderRepository implements OrderRepository {

    private final OrderMapper orderMapper;
    private final QueryOrderRepository queryOrderRepository;

    public Order save(Order order) {
        OrderEntity entity = orderMapper.OrderToOrderEntity(order);
        return orderMapper.OrderEntityToOrder(queryOrderRepository.save(entity));
    }

    public Optional<Order> findById(Long id) {
        return queryOrderRepository.findById(id).map(orderMapper::OrderEntityToOrder);
    }

    public List<Order> findAll() {
        return queryOrderRepository.findAll().stream().map(orderMapper::OrderEntityToOrder).toList();
    }

    public List<Order> findByUserId(UUID userId) {
        return queryOrderRepository.findByUser_Id(userId).stream().map(orderMapper::OrderEntityToOrder).toList();
    }

    public void deleteById(Long id) {
        queryOrderRepository.deleteById(id);
    }
}
