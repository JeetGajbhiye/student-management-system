package com.sms.studentmanagement.service;

import com.sms.studentmanagement.dto.DepartmentDto;

import java.util.List;

public interface DepartmentService {
    DepartmentDto.Response createDepartment(DepartmentDto.Request request);
    DepartmentDto.Response getDepartmentById(Long id);
    DepartmentDto.Response updateDepartment(Long id, DepartmentDto.Request request);
    void deleteDepartment(Long id);
    List<DepartmentDto.Response> getAllDepartments();
}
