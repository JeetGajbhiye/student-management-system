package com.sms.studentmanagement.mapper;

import com.sms.studentmanagement.dto.StudentDto;
import com.sms.studentmanagement.entity.Student;
import org.mapstruct.*;

/**
 * MapStruct mapper between {@link Student} entity and DTOs.
 * componentModel = "spring" is set globally via compiler arg.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {

    @Mapping(source = "department.id",   target = "departmentId")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(expression = "java(student.getEnrollments() != null ? student.getEnrollments().size() : 0)",
             target = "totalEnrollments")
    StudentDto.Response toResponse(Student student);

    @Mapping(target = "id",          ignore = true)
    @Mapping(target = "studentId",   ignore = true)
    @Mapping(target = "department",  ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    Student toEntity(StudentDto.Request request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",          ignore = true)
    @Mapping(target = "studentId",   ignore = true)
    @Mapping(target = "department",  ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    void updateEntityFromRequest(StudentDto.Request request, @MappingTarget Student student);
}
