package com.sms.studentmanagement.serviceimpl;

import com.sms.studentmanagement.dto.EnrollmentDto;
import com.sms.studentmanagement.dto.GradeDto;
import com.sms.studentmanagement.dto.PagedResponse;
import com.sms.studentmanagement.entity.Course;
import com.sms.studentmanagement.entity.Enrollment;
import com.sms.studentmanagement.entity.Enrollment.EnrollmentStatus;
import com.sms.studentmanagement.entity.Grade;
import com.sms.studentmanagement.entity.Student;
import com.sms.studentmanagement.exception.DuplicateResourceException;
import com.sms.studentmanagement.exception.ResourceNotFoundException;
import com.sms.studentmanagement.repository.CourseRepository;
import com.sms.studentmanagement.repository.EnrollmentRepository;
import com.sms.studentmanagement.repository.StudentRepository;
import com.sms.studentmanagement.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public EnrollmentDto.Response enrollStudent(EnrollmentDto.Request request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", request.getStudentId()));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", request.getCourseId()));

        if (enrollmentRepository.existsByStudentIdAndCourseId(request.getStudentId(), request.getCourseId())) {
            throw new DuplicateResourceException("Student is already enrolled in this course");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .enrollmentDate(request.getEnrollmentDate() != null ? request.getEnrollmentDate() : LocalDate.now())
                .status(EnrollmentStatus.ACTIVE)
                .build();

        Enrollment saved = enrollmentRepository.save(enrollment);
        log.info("Student {} enrolled in course {}", student.getStudentId(), course.getCode());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentDto.Response getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", id));
        return toResponse(enrollment);
    }

    @Override
    @Transactional
    public EnrollmentDto.Response updateEnrollmentStatus(Long id, EnrollmentDto.StatusUpdateRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", id));
        enrollment.setStatus(request.getStatus());
        Enrollment updated = enrollmentRepository.save(enrollment);
        log.info("Enrollment {} status updated to {}", id, request.getStatus());
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteEnrollment(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", id));
        enrollmentRepository.delete(enrollment);
        log.info("Enrollment deleted: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<EnrollmentDto.Response> getAllEnrollments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Enrollment> enrollments = enrollmentRepository.findAll(pageable);
        List<EnrollmentDto.Response> content = enrollments.getContent().stream()
                .map(this::toResponse).collect(Collectors.toList());
        return PagedResponse.<EnrollmentDto.Response>builder()
                .content(content).pageNumber(enrollments.getNumber()).pageSize(enrollments.getSize())
                .totalElements(enrollments.getTotalElements()).totalPages(enrollments.getTotalPages())
                .last(enrollments.isLast()).build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDto.Response> getEnrollmentsByStudent(Long studentId) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        return enrollmentRepository.findByStudentIdWithDetails(studentId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDto.Response> getEnrollmentsByCourse(Long courseId) {
        courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<EnrollmentDto.Response> getEnrollmentsByStatus(EnrollmentStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Enrollment> enrollments = enrollmentRepository.findByStatus(status, pageable);
        List<EnrollmentDto.Response> content = enrollments.getContent().stream()
                .map(this::toResponse).collect(Collectors.toList());
        return PagedResponse.<EnrollmentDto.Response>builder()
                .content(content).pageNumber(enrollments.getNumber()).pageSize(enrollments.getSize())
                .totalElements(enrollments.getTotalElements()).totalPages(enrollments.getTotalPages())
                .last(enrollments.isLast()).build();
    }

    private EnrollmentDto.Response toResponse(Enrollment e) {
        GradeDto.Response gradeResponse = null;
        Grade grade = e.getGrade();
        if (grade != null) {
            gradeResponse = GradeDto.Response.builder()
                    .id(grade.getId())
                    .enrollmentId(e.getId())
                    .marksObtained(grade.getMarksObtained())
                    .maxMarks(grade.getMaxMarks())
                    .percentage(grade.getPercentage())
                    .gradeLetter(grade.getGradeLetter())
                    .remarks(grade.getRemarks())
                    .examDate(grade.getExamDate())
                    .createdAt(grade.getCreatedAt())
                    .updatedAt(grade.getUpdatedAt())
                    .build();
        }
        String studentName = e.getStudent().getFirstName() + " " + e.getStudent().getLastName();
        return EnrollmentDto.Response.builder()
                .id(e.getId())
                .studentId(e.getStudent().getId())
                .studentName(studentName)
                .studentIdNumber(e.getStudent().getStudentId())
                .courseId(e.getCourse().getId())
                .courseTitle(e.getCourse().getTitle())
                .courseCode(e.getCourse().getCode())
                .enrollmentDate(e.getEnrollmentDate())
                .status(e.getStatus())
                .grade(gradeResponse)
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
