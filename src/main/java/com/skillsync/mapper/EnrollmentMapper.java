package com.skillsync.mapper;

import com.skillsync.dto.EnrollmentDto;
import com.skillsync.entity.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "course.id", target = "courseId")
    EnrollmentDto toDto(Enrollment enrollment);
}
