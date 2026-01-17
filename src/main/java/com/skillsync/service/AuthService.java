package com.skillsync.service;

import com.skillsync.dto.AuthRequest;
import com.skillsync.dto.AuthResponse;
import com.skillsync.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(AuthRequest request);
}
