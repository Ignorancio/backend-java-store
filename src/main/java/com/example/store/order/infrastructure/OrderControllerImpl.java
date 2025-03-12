package com.example.store.order.infrastructure;

import com.example.store.order.application.OrderServiceImpl;
import com.example.store.order.domain.Order;
import com.example.store.order.infrastructure.dto.OrderDTO;
import com.example.store.order.infrastructure.dto.OrderResponseAdminDTO;
import com.example.store.order.infrastructure.dto.OrderResponseDTO;
import com.example.store.order.infrastructure.mapper.OrderMapper;
import com.example.store.user.infrastructure.entity.UserEntity;
import com.example.store.user.infrastructure.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController{

    private final OrderServiceImpl orderService;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;

    @PostMapping("/")
    public ResponseEntity<OrderResponseDTO> save(@RequestBody @Valid OrderDTO orderDTO) {
        Order order = orderMapper.OrderDTOToOrder(orderDTO);
        Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(auth instanceof UserEntity){
            order.setUser(userMapper.userEntityToUser((UserEntity) auth));
            return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.OrderToOrderResponseDTO(orderService.save(order)));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<OrderResponseDTO>> findByUserId() {
        Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(auth instanceof UserEntity){
            List<Order> orders = orderService.findByUserId(((UserEntity)auth).getId());
            return ResponseEntity.status(HttpStatus.OK).body(orders.stream().map(orderMapper::OrderToOrderResponseDTO).toList());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseAdminDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findAll().stream().map(orderMapper::OrderToOrderResponseAdminDTO).toList());
    }
}
