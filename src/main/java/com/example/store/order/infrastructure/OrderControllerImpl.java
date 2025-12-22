package com.example.store.order.infrastructure;

import com.example.store.order.application.OrderServiceImpl;
import com.example.store.order.domain.Order;
import com.example.store.order.domain.OrderDetails;
import com.example.store.order.infrastructure.dto.OrderDTO;
import com.example.store.order.infrastructure.mapper.OrderMapper;
import com.example.store.user.infrastructure.entity.UserEntity;
import com.example.store.user.infrastructure.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order Services", description = "Operations related to orders")
public class OrderControllerImpl implements OrderController {

    private final OrderServiceImpl orderService;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;

    @PostMapping
    @Operation(summary = "Create a new order")
    public ResponseEntity<Order> save(@AuthenticationPrincipal UserEntity userAuth, @RequestBody @Valid OrderDTO orderDTO) {
        Order order = orderMapper.OrderDTOToOrder(orderDTO);
        order.setUser(userMapper.userEntityToUser(userAuth));
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.save(order));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Return an order by order id, but you must be the owner or an administrator")
    public ResponseEntity<Order> findById(@AuthenticationPrincipal UserEntity userAuth, @PathVariable Long id) {
        Order order = orderService.findById(id);

        checkAuthority(order, userAuth);

        return ResponseEntity.ok(order);
    }

    @GetMapping
    @Operation(summary = "Returns a list of all orders associated with the authenticated user")
    public ResponseEntity<List<Order>> findByUserId(@AuthenticationPrincipal UserEntity userAuth) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findByUserId(userAuth.getId()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a order by id, but you must be the owner or an administrator")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserEntity userAuth, @PathVariable Long id) {
        Order order = orderService.findById(id);

        checkAuthority(order, userAuth);

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
    public ResponseEntity<Order> update(@AuthenticationPrincipal UserEntity userAuth, @PathVariable Long id, @RequestBody @Valid OrderDTO orderDTO) {
        Order existingOrder = orderService.findById(id);

        checkAuthority(existingOrder, userAuth);

        List<OrderDetails> details = orderDTO.orderDetails().stream()
                .map(orderMapper::orderdetailsDTOToOrderDetails)
                .toList();

        return ResponseEntity.ok(orderService.addOrderDetails(id, details));
    }

    private void checkAuthority(Order order, UserEntity userAuth) {
        boolean isOwner = order.getUser().getId().equals(userAuth.getId());
        boolean isAdmin = userAuth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
            // Lanzar una excepción personalizada o AccessDeniedException de Spring Security
            throw new AccessDeniedException("No tienes permiso para acceder a este pedido");
        }
    }
}
