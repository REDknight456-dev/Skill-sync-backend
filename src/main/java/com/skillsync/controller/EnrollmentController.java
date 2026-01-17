package com.skillsync.controller;

import com.skillsync.dto.EnrollmentDto;
import com.skillsync.dto.UpdateProgressRequest;
import com.skillsync.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/{courseId}")
    public ResponseEntity<EnrollmentDto> enroll(@PathVariable("courseId") Long courseId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollmentService.enrollInCourse(courseId));
    }

    @GetMapping("/me")
    public ResponseEntity<List<EnrollmentDto>> myEnrollments() {
        return ResponseEntity.ok(enrollmentService.getMyEnrollments());
    }

    @PatchMapping("/{enrollmentId}/progress")
    public ResponseEntity<EnrollmentDto> updateProgress(@PathVariable("enrollmentId") Long enrollmentId,
                                                         @Valid @RequestBody UpdateProgressRequest request) {
        return ResponseEntity.ok(enrollmentService.updateProgress(enrollmentId, request.progressPercent()));
    }
}
