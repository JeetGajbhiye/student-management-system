package com.sms.studentmanagement.mapper;

import com.sms.studentmanagement.dto.DepartmentDto;
import com.sms.studentmanagement.entity.Department;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartmentMapper {

    @Mapping(expression = "java(dept.getStudents() != null ? dept.getStudents().size() : 0)", target = "totalStudents")
    @Mapping(expression = "java(dept.getCourses()  != null ? dept.getCourses().size()  : 0)", target = "totalCourses")
    DepartmentDto.Response toResponse(Department dept);

    @Mapping(target = "id",       ignore = true)
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "courses",  ignore = true)
    Department toEntity(DepartmentDto.Request request);
}
