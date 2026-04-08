package com.sms.studentmanagement.service;

import com.sms.studentmanagement.dto.EnrollmentDto;
import com.sms.studentmanagement.dto.PagedResponse;
import com.sms.studentmanagement.entity.Enrollment.EnrollmentStatus;

import java.util.List;

public interface EnrollmentService {
    EnrollmentDto.Response enrollStudent(EnrollmentDto.Request request);
    EnrollmentDto.Response getEnrollmentById(Long id);
    EnrollmentDto.Response updateEnrollmentStatus(Long id, EnrollmentDto.StatusUpdateRequest request);
    void deleteEnrollment(Long id);
    PagedResponse<EnrollmentDto.Response> getAllEnrollments(int page, int size);
    List<EnrollmentDto.Response> getEnrollmentsByStudent(Long studentId);
    List<EnrollmentDto.Response> getEnrollmentsByCourse(Long courseId);
    PagedResponse<EnrollmentDto.Response> getEnrollmentsByStatus(EnrollmentStatus status, int page, int size);
}
