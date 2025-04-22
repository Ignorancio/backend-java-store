package com.example.store.order.infrastructure;

import com.example.store.order.application.OrderServiceImpl;
import com.example.store.order.domain.Order;
import com.example.store.order.infrastructure.dto.OrderDTO;
import com.example.store.order.infrastructure.dto.OrderDetailsDTO;
import com.example.store.order.infrastructure.mapper.OrderMapper;
import com.example.store.user.domain.Role;
import com.example.store.user.infrastructure.entity.UserEntity;
import com.example.store.user.infrastructure.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Order Services",description = "Operations related to orders")
public class OrderControllerImpl implements OrderController{

    private final OrderServiceImpl orderService;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;

    @PostMapping
    @Operation(summary = "Create a new order")
    public ResponseEntity<Order> save(@RequestBody @Valid OrderDTO orderDTO) {
        Order order = orderMapper.OrderDTOToOrder(orderDTO);
        Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(auth instanceof UserEntity userEntity){
            order.setUser(userMapper.userEntityToUser(userEntity));
            return ResponseEntity.status(HttpStatus.CREATED).body(orderService.save(order));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Return an order by order id, but you must be the owner or an administrator")
    public ResponseEntity<Order> findById(@PathVariable Long id) {
        Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(auth instanceof UserEntity userAuth)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Order order = orderService.findById(id);
        if (!order.getUser().getId().equals(userAuth.getId()) && !userAuth.getRoles().contains(Role.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @GetMapping
    @Operation(summary = "Returns a list of all orders associated with the authenticated user")
    public ResponseEntity<List<Order>> findByUserId() {
        Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(auth instanceof UserEntity userAuth){
            return ResponseEntity.status(HttpStatus.OK).body(orderService.findByUserId(userAuth.getId()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a order by id, but you must be the owner or an administrator")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(auth instanceof UserEntity userAuth)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Order order = orderService.findById(id);
        if (!order.getUser().getId().equals(userAuth.getId()) && !userAuth.getRoles().contains(Role.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        orderService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Retrieve all orders. This action is restricted to administrators only")
    public ResponseEntity<List<Order>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an order by id, but you must be the owner or an administrator")
    public ResponseEntity<Order> update(@PathVariable Long id, @RequestBody @Valid List<OrderDetailsDTO> orderDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.addOrderDetails(id, orderDTO.stream().map(orderMapper::orderdetailsDTOToOrderDetails).toList()));
    }
}
