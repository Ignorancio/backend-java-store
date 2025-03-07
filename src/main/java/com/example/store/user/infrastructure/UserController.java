package com.example.store.user.infrastructure;

import com.example.store.user.domain.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UserController {

    ResponseEntity<User> update(User user);

    ResponseEntity<List<User>> findAll();

    ResponseEntity<User> findById(UUID id);

    ResponseEntity<Void> delete(UUID id);
}
