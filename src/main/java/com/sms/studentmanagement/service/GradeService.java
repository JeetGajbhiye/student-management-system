package com.sms.studentmanagement.service;

import com.sms.studentmanagement.dto.GradeDto;

import java.util.List;

public interface GradeService {
    GradeDto.Response assignGrade(GradeDto.Request request);
    GradeDto.Response getGradeById(Long id);
    GradeDto.Response getGradeByEnrollment(Long enrollmentId);
    GradeDto.Response updateGrade(Long id, GradeDto.Request request);
    void deleteGrade(Long id);
    List<GradeDto.Response> getGradesByStudent(Long studentId);
    List<GradeDto.Response> getGradesByCourse(Long courseId);
}
