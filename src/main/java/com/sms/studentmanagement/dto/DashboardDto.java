package com.sms.studentmanagement.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDto {
    private long totalStudents;
    private long totalCourses;
    private long totalDepartments;
    private long totalEnrollments;
    private long activeEnrollments;
    private Map<String, Long> studentsByDepartment;
    private Map<String, Long> enrollmentsByStatus;
    private Map<String, Double> averageGradeByDepartment;
}
