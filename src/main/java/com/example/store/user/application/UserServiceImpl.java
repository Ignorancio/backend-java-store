package com.example.store.user.application;

import com.example.store.user.domain.User;
import com.example.store.user.domain.UserService;
import com.example.store.user.infrastructure.repository.implementation.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public User update(User user) {
        return null;
    }

    public List<User> findAll() {
        return null;
    }

    public User findById(String id) {
        return null;
    }

    public void delete(String id) {

    }
}
