package com.sms.studentmanagement.mapper;

import com.sms.studentmanagement.dto.CourseDto;
import com.sms.studentmanagement.entity.Course;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper {

    @Mapping(source = "department.id",   target = "departmentId")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(expression = "java(course.getEnrollments() != null ? course.getEnrollments().size() : 0)",
             target = "totalEnrollments")
    CourseDto.Response toResponse(Course course);

    @Mapping(target = "id",          ignore = true)
    @Mapping(target = "department",  ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    Course toEntity(CourseDto.Request request);
}
