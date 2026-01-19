package com.skillsync.dto;

public record TwoFactorLoginResponse(boolean requires2fa, String twoFactorToken, String message, String email) {
}
