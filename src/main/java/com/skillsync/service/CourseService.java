package com.skillsync.service;

import com.skillsync.dto.CourseDto;

import java.util.List;

public interface CourseService {
    CourseDto createCourse(CourseDto request);
    CourseDto updateCourse(Long id, CourseDto request);
    void deleteCourse(Long id);
    List<CourseDto> getCourses();
    CourseDto getCourse(Long id);
}
