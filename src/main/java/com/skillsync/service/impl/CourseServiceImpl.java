package com.skillsync.service.impl;

import com.skillsync.dto.CourseDto;
import com.skillsync.entity.Course;
import com.skillsync.event.CourseCreatedEvent;
import com.skillsync.mapper.CourseMapper;
import com.skillsync.repository.CourseRepository;
import com.skillsync.service.CourseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${app.mail.admin:}")
    private String adminEmailsProperty;

    public CourseServiceImpl(CourseRepository courseRepository,
                             CourseMapper courseMapper,
                             ApplicationEventPublisher eventPublisher) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.eventPublisher = eventPublisher;
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
        List<String> recipients = resolveAdminEmails();
        if (!recipients.isEmpty()) {
            eventPublisher.publishEvent(new CourseCreatedEvent(saved.getTitle(), saved.getInstructor(), recipients));
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

    private List<String> resolveAdminEmails() {
        Set<String> recipients = new LinkedHashSet<>();
        recipients.add("becha.rabie@gmail.com");
        if (adminEmailsProperty != null && !adminEmailsProperty.isBlank()) {
            Stream.of(adminEmailsProperty.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .forEach(recipients::add);
        }
        return List.copyOf(recipients);
    }
}
