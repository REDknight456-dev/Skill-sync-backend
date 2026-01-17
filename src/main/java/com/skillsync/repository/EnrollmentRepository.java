package com.skillsync.repository;

import com.skillsync.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByUserId(Long userId);
    List<Enrollment> findByCourseId(Long courseId);
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
    Optional<Enrollment> findByIdAndUserId(Long id, Long userId);
}
