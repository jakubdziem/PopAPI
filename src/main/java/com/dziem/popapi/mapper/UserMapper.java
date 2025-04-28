package com.dziem.popapi.mapper;

import com.dziem.popapi.model.User;
import com.dziem.popapi.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface UserMapper {
    @Mapping(source = "UName.name", target = "name")
    UserDTO userToUserDTO(User user);
}
