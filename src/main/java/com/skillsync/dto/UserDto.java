package com.skillsync.dto;

import com.skillsync.entity.Role;

public record UserDto(Long id, String email, Role role) {
}
