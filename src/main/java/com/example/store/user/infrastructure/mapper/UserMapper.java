package com.example.store.user.infrastructure.mapper;

import com.example.store.user.domain.User;
import com.example.store.user.infrastructure.dto.UserDTO;
import com.example.store.user.infrastructure.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    User userEntityToUser(UserEntity userEntity);

    UserEntity userToUserEntity(User user);

    UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);

    //User registerRequestToUser(RegisterRequest registerRequest);

}
