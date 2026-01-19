package com.skillsync.service.impl;

import com.skillsync.dto.CourseDto;
import com.skillsync.entity.Course;
import com.skillsync.mapper.CourseMapper;
import com.skillsync.repository.CourseRepository;
import com.skillsync.service.CourseService;
import com.skillsync.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final EmailService emailService;
    private final CourseMapper courseMapper;

    @Value("${app.mail.admin:}")
    private String adminEmail;

    public CourseServiceImpl(CourseRepository courseRepository, EmailService emailService, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.emailService = emailService;
        this.courseMapper = courseMapper;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public CourseDto createCourse(CourseDto request) {
        Course course = courseMapper.toEntity(request);
        if (course.getPrice() == null) {
            course.setPrice(BigDecimal.ZERO);
        }
        Course saved = courseRepository.save(course);
        if (!adminEmail.isBlank()) {
            emailService.sendCourseCreatedAdmin(adminEmail, saved.getTitle(), saved.getInstructor());
        }
        return courseMapper.toDto(saved);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public CourseDto updateCourse(Long id, CourseDto request) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        existing.setTitle(request.title());
        existing.setDescription(request.description());
        existing.setInstructor(request.instructor());
        existing.setPrice(request.price() == null ? BigDecimal.ZERO : request.price());

        Course saved = courseRepository.save(existing);
        return courseMapper.toDto(saved);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<CourseDto> getCourses() {
        return courseRepository.findAll().stream().map(courseMapper::toDto).toList();
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public CourseDto getCourse(Long id) {
        return courseRepository.findById(id).map(courseMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
    }
}
