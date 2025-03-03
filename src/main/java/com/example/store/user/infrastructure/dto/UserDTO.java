package com.example.store.user.infrastructure.dto;

import com.example.store.user.domain.Role;

import java.util.List;
import java.util.UUID;

public record UserDTO(UUID id,
                      String email,
                      String password,
                      List<Role> role
                      ) {
}
