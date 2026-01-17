package com.skillsync.service.impl;

import com.skillsync.dto.EnrollmentDto;
import com.skillsync.entity.Course;
import com.skillsync.entity.Enrollment;
import com.skillsync.entity.User;
import com.skillsync.repository.CourseRepository;
import com.skillsync.repository.EnrollmentRepository;
import com.skillsync.repository.UserRepository;
import com.skillsync.service.EnrollmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository,
                                 CourseRepository courseRepository,
                                 UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public EnrollmentDto enrollInCourse(Long courseId) {
        User user = currentUser();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (enrollmentRepository.existsByUserIdAndCourseId(user.getId(), courseId)) {
            throw new IllegalArgumentException("Already enrolled in this course");
        }

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .createdAt(Instant.now())
                .progressPercent(0)
                .build();

        Enrollment saved = enrollmentRepository.save(enrollment);
        return toDto(saved);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<EnrollmentDto> getMyEnrollments() {
        User user = currentUser();
        return enrollmentRepository.findByUserId(user.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public EnrollmentDto updateProgress(Long enrollmentId, int progressPercent) {
        if (progressPercent < 0 || progressPercent > 100) {
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        }
        User user = currentUser();
        Enrollment enrollment = enrollmentRepository.findByIdAndUserId(enrollmentId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found for user"));
        enrollment.setProgressPercent(progressPercent);
        Enrollment saved = enrollmentRepository.save(enrollment);
        return toDto(saved);
    }

    private EnrollmentDto toDto(Enrollment enrollment) {
        return new EnrollmentDto(
                enrollment.getId(),
                enrollment.getUser().getId(),
                enrollment.getCourse().getId(),
                enrollment.getCreatedAt(),
                enrollment.getProgressPercent()
        );
    }

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new IllegalArgumentException("Unauthenticated user");
        }
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
