package com.skillsync.mapper;

import com.skillsync.dto.UserDto;
import com.skillsync.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
