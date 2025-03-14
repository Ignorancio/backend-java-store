package com.example.store.user.infrastructure.dto;

import com.example.store.user.domain.Role;

import java.util.Set;
import java.util.UUID;

public record UserDTO(UUID id,
                      String email,
                      Set<Role> roles
                      ) {
}
