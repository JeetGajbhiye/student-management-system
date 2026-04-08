package com.sms.studentmanagement.controller;

import com.sms.studentmanagement.dto.ApiResponse;
import com.sms.studentmanagement.dto.GradeDto;
import com.sms.studentmanagement.service.GradeService;
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
@RequestMapping("/api/grades")
@RequiredArgsConstructor
@Tag(name = "Grades", description = "Grade management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Assign grade to an enrollment")
    public ResponseEntity<ApiResponse<GradeDto.Response>> assignGrade(
            @Valid @RequestBody GradeDto.Request request) {
        GradeDto.Response grade = gradeService.assignGrade(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Grade assigned successfully", grade));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get grade by ID")
    public ResponseEntity<ApiResponse<GradeDto.Response>> getGradeById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(gradeService.getGradeById(id)));
    }

    @GetMapping("/enrollment/{enrollmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get grade by enrollment ID")
    public ResponseEntity<ApiResponse<GradeDto.Response>> getGradeByEnrollment(@PathVariable Long enrollmentId) {
        return ResponseEntity.ok(ApiResponse.success(gradeService.getGradeByEnrollment(enrollmentId)));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get all grades for a student")
    public ResponseEntity<ApiResponse<List<GradeDto.Response>>> getGradesByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(gradeService.getGradesByStudent(studentId)));
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get all grades for a course")
    public ResponseEntity<ApiResponse<List<GradeDto.Response>>> getGradesByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(ApiResponse.success(gradeService.getGradesByCourse(courseId)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Update a grade")
    public ResponseEntity<ApiResponse<GradeDto.Response>> updateGrade(
            @PathVariable Long id,
            @Valid @RequestBody GradeDto.Request request) {
        GradeDto.Response updated = gradeService.updateGrade(id, request);
        return ResponseEntity.ok(ApiResponse.success("Grade updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a grade")
    public ResponseEntity<ApiResponse<Void>> deleteGrade(@PathVariable Long id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.ok(ApiResponse.success("Grade deleted successfully", null));
    }
}
