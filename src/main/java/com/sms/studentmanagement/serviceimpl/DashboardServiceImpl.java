package com.sms.studentmanagement.serviceimpl;

import com.sms.studentmanagement.dto.DashboardDto;
import com.sms.studentmanagement.entity.Enrollment.EnrollmentStatus;
import com.sms.studentmanagement.repository.*;
import com.sms.studentmanagement.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final GradeRepository gradeRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardDto getDashboardStats() {
        long totalStudents = studentRepository.count();
        long totalCourses = courseRepository.count();
        long totalDepartments = departmentRepository.count();
        long totalEnrollments = enrollmentRepository.count();
        long activeEnrollments = enrollmentRepository.countByStatus(EnrollmentStatus.ACTIVE);

        // Students by department
        Map<String, Long> studentsByDept = new LinkedHashMap<>();
        List<Object[]> deptData = studentRepository.countStudentsByDepartment();
        for (Object[] row : deptData) {
            studentsByDept.put((String) row[0], (Long) row[1]);
        }

        // Enrollments by status
        Map<String, Long> enrollmentsByStatus = new LinkedHashMap<>();
        List<Object[]> statusData = enrollmentRepository.countEnrollmentsByStatus();
        for (Object[] row : statusData) {
            enrollmentsByStatus.put(row[0].toString(), (Long) row[1]);
        }

        // Average grade by department
        Map<String, Double> avgGradeByDept = new LinkedHashMap<>();
        List<Object[]> gradeData = gradeRepository.findAverageGradeByDepartment();
        for (Object[] row : gradeData) {
            if (row[1] != null) {
                avgGradeByDept.put((String) row[0], Math.round((Double) row[1] * 100.0) / 100.0);
            }
        }

        return DashboardDto.builder()
                .totalStudents(totalStudents)
                .totalCourses(totalCourses)
                .totalDepartments(totalDepartments)
                .totalEnrollments(totalEnrollments)
                .activeEnrollments(activeEnrollments)
                .studentsByDepartment(studentsByDept)
                .enrollmentsByStatus(enrollmentsByStatus)
                .averageGradeByDepartment(avgGradeByDept)
                .build();
    }
}
