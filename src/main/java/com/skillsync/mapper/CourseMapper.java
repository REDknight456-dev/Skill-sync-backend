package com.skillsync.mapper;

import com.skillsync.dto.CourseDto;
import com.skillsync.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mapping(target = "enrollments", ignore = true)
    CourseDto toDto(Course course);

    @Mapping(target = "enrollments", ignore = true)
    Course toEntity(CourseDto dto);
}
