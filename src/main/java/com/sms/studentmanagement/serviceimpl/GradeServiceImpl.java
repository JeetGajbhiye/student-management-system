package com.sms.studentmanagement.serviceimpl;

import com.sms.studentmanagement.dto.GradeDto;
import com.sms.studentmanagement.entity.Enrollment;
import com.sms.studentmanagement.entity.Grade;
import com.sms.studentmanagement.exception.BadRequestException;
import com.sms.studentmanagement.exception.DuplicateResourceException;
import com.sms.studentmanagement.exception.ResourceNotFoundException;
import com.sms.studentmanagement.repository.EnrollmentRepository;
import com.sms.studentmanagement.repository.GradeRepository;
import com.sms.studentmanagement.service.GradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    @Transactional
    public GradeDto.Response assignGrade(GradeDto.Request request) {
        if (gradeRepository.existsByEnrollmentId(request.getEnrollmentId())) {
            throw new DuplicateResourceException("Grade already assigned for this enrollment. Use update instead.");
        }

        Enrollment enrollment = enrollmentRepository.findById(request.getEnrollmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", request.getEnrollmentId()));

        if (request.getMarksObtained() > request.getMaxMarks()) {
            throw new BadRequestException("Marks obtained cannot exceed max marks");
        }

        Grade grade = Grade.builder()
                .enrollment(enrollment)
                .marksObtained(request.getMarksObtained())
                .maxMarks(request.getMaxMarks())
                .gradeLetter(request.getGradeLetter() != null ? request.getGradeLetter() : computeGradeLetter(request.getMarksObtained(), request.getMaxMarks()))
                .remarks(request.getRemarks())
                .examDate(request.getExamDate())
                .build();

        Grade saved = gradeRepository.save(grade);
        log.info("Grade assigned for enrollment: {}", request.getEnrollmentId());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public GradeDto.Response getGradeById(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade", "id", id));
        return toResponse(grade);
    }

    @Override
    @Transactional(readOnly = true)
    public GradeDto.Response getGradeByEnrollment(Long enrollmentId) {
        Grade grade = gradeRepository.findByEnrollmentId(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Grade", "enrollmentId", enrollmentId));
        return toResponse(grade);
    }

    @Override
    @Transactional
    public GradeDto.Response updateGrade(Long id, GradeDto.Request request) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade", "id", id));

        if (request.getMarksObtained() > request.getMaxMarks()) {
            throw new BadRequestException("Marks obtained cannot exceed max marks");
        }

        grade.setMarksObtained(request.getMarksObtained());
        grade.setMaxMarks(request.getMaxMarks());
        grade.setGradeLetter(request.getGradeLetter() != null ? request.getGradeLetter() : computeGradeLetter(request.getMarksObtained(), request.getMaxMarks()));
        grade.setRemarks(request.getRemarks());
        grade.setExamDate(request.getExamDate());

        Grade updated = gradeRepository.save(grade);
        log.info("Grade updated: {}", id);
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteGrade(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade", "id", id));
        gradeRepository.delete(grade);
        log.info("Grade deleted: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradeDto.Response> getGradesByStudent(Long studentId) {
        return gradeRepository.findByStudentId(studentId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradeDto.Response> getGradesByCourse(Long courseId) {
        return gradeRepository.findByCourseId(courseId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    private String computeGradeLetter(double marks, double maxMarks) {
        double pct = (marks / maxMarks) * 100;
        if (pct >= 90) return "A+";
        if (pct >= 80) return "A";
        if (pct >= 70) return "B";
        if (pct >= 60) return "C";
        if (pct >= 50) return "D";
        return "F";
    }

    private GradeDto.Response toResponse(Grade g) {
        String studentName = g.getEnrollment().getStudent().getFirstName() + " " + g.getEnrollment().getStudent().getLastName();
        return GradeDto.Response.builder()
                .id(g.getId())
                .enrollmentId(g.getEnrollment().getId())
                .studentName(studentName)
                .courseTitle(g.getEnrollment().getCourse().getTitle())
                .marksObtained(g.getMarksObtained())
                .maxMarks(g.getMaxMarks())
                .percentage(g.getPercentage())
                .gradeLetter(g.getGradeLetter())
                .remarks(g.getRemarks())
                .examDate(g.getExamDate())
                .createdAt(g.getCreatedAt())
                .updatedAt(g.getUpdatedAt())
                .build();
    }
}
