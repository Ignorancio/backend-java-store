package com.example.store.user.infrastructure;

import com.example.store.user.domain.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UserController {

    ResponseEntity<User> updateUser(User user);

    ResponseEntity<List<User>> getUsers();

    ResponseEntity<User> getUserById(UUID id);

    ResponseEntity<Void> deleteUser(UUID id);
}
