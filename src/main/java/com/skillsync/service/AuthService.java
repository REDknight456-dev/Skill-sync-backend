package com.skillsync.service;

import com.skillsync.dto.AuthRequest;
import com.skillsync.dto.AuthResponse;
import com.skillsync.dto.RegisterRequest;
import com.skillsync.dto.TwoFactorVerifyRequest;
import com.skillsync.dto.TwoFactorLoginResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    TwoFactorLoginResponse login(AuthRequest request);
    AuthResponse verifyTwoFactor(TwoFactorVerifyRequest request);
}
