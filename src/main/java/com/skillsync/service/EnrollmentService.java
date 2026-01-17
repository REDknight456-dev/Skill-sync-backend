package com.skillsync.service;

import com.skillsync.dto.EnrollmentDto;

import java.util.List;

public interface EnrollmentService {
    EnrollmentDto enrollInCourse(Long courseId);
    List<EnrollmentDto> getMyEnrollments();
    EnrollmentDto updateProgress(Long enrollmentId, int progressPercent);
}
