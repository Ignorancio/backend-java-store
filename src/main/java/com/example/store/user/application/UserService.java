package com.example.store.user.application;

import com.example.store.user.domain.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User update(User user);

    List<User> findAll();

    User findById(UUID id);

    void delete(UUID id);
}
