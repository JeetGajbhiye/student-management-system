package com.sms.studentmanagement.controller;

import com.sms.studentmanagement.dto.ApiResponse;
import com.sms.studentmanagement.dto.EnrollmentDto;
import com.sms.studentmanagement.dto.PagedResponse;
import com.sms.studentmanagement.entity.Enrollment.EnrollmentStatus;
import com.sms.studentmanagement.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@Tag(name = "Enrollments", description = "Student course enrollment endpoints")
@SecurityRequirement(name = "bearerAuth")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Enroll a student in a course")
    public ResponseEntity<ApiResponse<EnrollmentDto.Response>> enrollStudent(
            @Valid @RequestBody EnrollmentDto.Request request) {
        EnrollmentDto.Response enrollment = enrollmentService.enrollStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Student enrolled successfully", enrollment));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get all enrollments with pagination")
    public ResponseEntity<ApiResponse<PagedResponse<EnrollmentDto.Response>>> getAllEnrollments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(enrollmentService.getAllEnrollments(page, size)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get enrollment by ID")
    public ResponseEntity<ApiResponse<EnrollmentDto.Response>> getEnrollmentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(enrollmentService.getEnrollmentById(id)));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get all enrollments for a student")
    public ResponseEntity<ApiResponse<List<EnrollmentDto.Response>>> getEnrollmentsByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(enrollmentService.getEnrollmentsByStudent(studentId)));
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get all enrollments for a course")
    public ResponseEntity<ApiResponse<List<EnrollmentDto.Response>>> getEnrollmentsByCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(ApiResponse.success(enrollmentService.getEnrollmentsByCourse(courseId)));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get enrollments by status")
    public ResponseEntity<ApiResponse<PagedResponse<EnrollmentDto.Response>>> getEnrollmentsByStatus(
            @PathVariable EnrollmentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(enrollmentService.getEnrollmentsByStatus(status, page, size)));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Update enrollment status")
    public ResponseEntity<ApiResponse<EnrollmentDto.Response>> updateEnrollmentStatus(
            @PathVariable Long id,
            @Valid @RequestBody EnrollmentDto.StatusUpdateRequest request) {
        EnrollmentDto.Response updated = enrollmentService.updateEnrollmentStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("Enrollment status updated", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete enrollment")
    public ResponseEntity<ApiResponse<Void>> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.ok(ApiResponse.success("Enrollment deleted successfully", null));
    }
}
