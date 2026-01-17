package com.skillsync.service;

import com.skillsync.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
}
