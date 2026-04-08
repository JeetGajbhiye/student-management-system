package com.sms.studentmanagement.service;

import com.sms.studentmanagement.dto.PagedResponse;
import com.sms.studentmanagement.dto.StudentDto;

import java.util.List;

public interface StudentService {
    StudentDto.Response createStudent(StudentDto.Request request);
    StudentDto.Response getStudentById(Long id);
    StudentDto.Response getStudentByStudentId(String studentId);
    StudentDto.Response updateStudent(Long id, StudentDto.Request request);
    void deleteStudent(Long id);
    PagedResponse<StudentDto.Response> getAllStudents(int page, int size, String sortBy, String sortDir);
    PagedResponse<StudentDto.Response> searchStudents(String keyword, int page, int size);
    PagedResponse<StudentDto.Response> getStudentsByDepartment(Long departmentId, int page, int size);
    List<StudentDto.Response> getAllStudentsAsList();
}
