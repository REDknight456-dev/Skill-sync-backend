package com.skillsync.mapper;

import com.skillsync.dto.CourseDto;
import com.skillsync.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper {
    CourseDto toDto(Course course);

    @Mapping(target = "enrollments", ignore = true)
    Course toEntity(CourseDto dto);
}
