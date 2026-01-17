package com.skillsync.mapper;

import com.skillsync.dto.CourseDto;
import com.skillsync.entity.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseDto toDto(Course course);
    Course toEntity(CourseDto dto);
}
