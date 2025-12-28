package com.example.store.user.application;

import com.example.store.shared.domain.exception.ResourceNotFoundException;
import com.example.store.user.domain.User;
import com.example.store.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public User update(User updateUser) {

        User user = userRepository.findById(updateUser.getId()).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        BeanUtils.copyProperties(updateUser, user, "role");

        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}
