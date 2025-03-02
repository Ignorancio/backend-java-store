package com.example.store.user.infrastructure.dto;

import com.example.store.user.domain.Role;

import java.util.List;

public record UserDTO(Long id,
                      String email,
                      String password,
                      List<Role> role
                      ) {
}
