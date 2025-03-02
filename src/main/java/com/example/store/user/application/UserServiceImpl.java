package com.example.store.user.application;

import com.example.store.user.domain.User;
import com.example.store.user.domain.UserRepository;
import com.example.store.user.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public User update(User updateUser) {

        User user = userRepository.findById(updateUser.getId()).orElseThrow(()-> new IllegalArgumentException("Usuario no encontrado"));

        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Usuario no encontrado"));
    }

    public void delete(UUID id) {
        userRepository.delete(id);
    }
}
