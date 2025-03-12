package com.example.store.order.domain;

public interface OrderDetailsRepository {

    OrderDetails save(OrderDetails orderDetails);

    void deleteByOrderId(Long orderId);
}
