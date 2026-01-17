package com.skillsync.dto;

import java.math.BigDecimal;

public record CourseDto(Long id, String title, String description, String instructor, BigDecimal price) {
}
