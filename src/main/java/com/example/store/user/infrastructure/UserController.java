package com.example.store.user.infrastructure;

import com.example.store.user.domain.User;
import com.example.store.user.infrastructure.dto.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UserController {

    ResponseEntity<User> update(User user);

    ResponseEntity<List<UserDTO>> findAll();

    ResponseEntity<UserDTO> findById(UUID id);

    ResponseEntity<Void> delete(UUID id);
}
