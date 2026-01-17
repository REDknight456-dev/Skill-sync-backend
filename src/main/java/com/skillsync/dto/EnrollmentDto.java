package com.skillsync.dto;

import java.time.Instant;

public record EnrollmentDto(Long id, Long userId, Long courseId, Instant createdAt, int progressPercent) {
}
