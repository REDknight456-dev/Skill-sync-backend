package com.skillsync.mapper;

import com.skillsync.dto.UserDto;
import com.skillsync.entity.Role;
import com.skillsync.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-19T01:00:31+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String email = null;
        Role role = null;

        id = user.getId();
        email = user.getEmail();
        role = user.getRole();

        UserDto userDto = new UserDto( id, email, role );

        return userDto;
    }
}
