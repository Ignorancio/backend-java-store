package com.example.store.repository;

import com.example.store.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {

}
