package com.example.store.user.domain;

import com.example.store.order.infrastructure.entity.OrderEntity;
import com.example.store.user.infrastructure.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class User {
    private UUID id;
    private String email;
    private String password;
    private Set<Role> roles;
    private List<OrderEntity> orders;
}
