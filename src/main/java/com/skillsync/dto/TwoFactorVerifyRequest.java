package com.skillsync.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TwoFactorVerifyRequest(
        @NotBlank String twoFactorToken,
        @NotBlank @Size(min = 6, max = 6) String code
) {
}
