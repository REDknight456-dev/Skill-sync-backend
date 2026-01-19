package com.skillsync.controller;

import com.skillsync.dto.CourseDto;
import com.skillsync.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<CourseDto>> listCourses() {
        return ResponseEntity.ok(courseService.getCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable("id") Long id) {
        return ResponseEntity.ok(courseService.getCourse(id));
    }

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable("id") Long id, @Valid @RequestBody CourseDto request) {
        return ResponseEntity.ok(courseService.updateCourse(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable("id") Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
