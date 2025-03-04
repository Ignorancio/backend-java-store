package com.example.store.user.infrastructure.repository.implementation;

import com.example.store.user.domain.User;
import com.example.store.user.domain.UserRepository;
import com.example.store.user.infrastructure.entity.UserEntity;
import com.example.store.user.infrastructure.mapper.UserMapper;
import com.example.store.user.infrastructure.repository.QueryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PostgresUserRepository implements UserRepository {

    private final QueryUserRepository queryUserRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User userEntity) {
        UserEntity entity = userMapper.userToUserEntity(userEntity);
        UserEntity savedEntity = queryUserRepository.save(entity);
        return userMapper.userEntityToUser(savedEntity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return queryUserRepository.findById(id).map(userMapper::userEntityToUser);
    }

    @Override
    public List<User> findAll() {
        return queryUserRepository.findAll().stream().map(userMapper::userEntityToUser).toList();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return queryUserRepository.findByEmail(email).map(userMapper::userEntityToUser);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return queryUserRepository.existsByEmail(email);
    }

    @Override
    public void delete(UUID id) {
        queryUserRepository.deleteById(id);
    }
}
