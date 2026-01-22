package com.skillsync.mapper;

import com.skillsync.dto.CourseDto;
import com.skillsync.entity.Course;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-22T19:21:52+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260101-2150, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class CourseMapperImpl implements CourseMapper {

    @Override
    public CourseDto toDto(Course course) {
        if ( course == null ) {
            return null;
        }

        Long id = null;
        String title = null;
        String description = null;
        String instructor = null;
        BigDecimal price = null;

        id = course.getId();
        title = course.getTitle();
        description = course.getDescription();
        instructor = course.getInstructor();
        price = course.getPrice();

        CourseDto courseDto = new CourseDto( id, title, description, instructor, price );

        return courseDto;
    }

    @Override
    public Course toEntity(CourseDto dto) {
        if ( dto == null ) {
            return null;
        }

        Course.CourseBuilder course = Course.builder();

        course.description( dto.description() );
        course.id( dto.id() );
        course.instructor( dto.instructor() );
        course.price( dto.price() );
        course.title( dto.title() );

        return course.build();
    }
}
