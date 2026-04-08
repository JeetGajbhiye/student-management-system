package com.sms.studentmanagement.controller;

import com.sms.studentmanagement.dto.ApiResponse;
import com.sms.studentmanagement.dto.CourseDto;
import com.sms.studentmanagement.dto.PagedResponse;
import com.sms.studentmanagement.service.CourseService;
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
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Course management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Create a new course")
    public ResponseEntity<ApiResponse<CourseDto.Response>> createCourse(
            @Valid @RequestBody CourseDto.Request request) {
        CourseDto.Response course = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Course created successfully", course));
    }

    @GetMapping
    @Operation(summary = "Get all courses with pagination")
    public ResponseEntity<ApiResponse<PagedResponse<CourseDto.Response>>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(ApiResponse.success(courseService.getAllCourses(page, size, sortBy, sortDir)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID")
    public ResponseEntity<ApiResponse<CourseDto.Response>> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(courseService.getCourseById(id)));
    }

    @GetMapping("/search")
    @Operation(summary = "Search courses by keyword")
    public ResponseEntity<ApiResponse<PagedResponse<CourseDto.Response>>> searchCourses(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(courseService.searchCourses(keyword, page, size)));
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get courses by department")
    public ResponseEntity<ApiResponse<List<CourseDto.Response>>> getCoursesByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(ApiResponse.success(courseService.getCoursesByDepartment(departmentId)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Update course")
    public ResponseEntity<ApiResponse<CourseDto.Response>> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseDto.Request request) {
        CourseDto.Response updated = courseService.updateCourse(id, request);
        return ResponseEntity.ok(ApiResponse.success("Course updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete course")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(ApiResponse.success("Course deleted successfully", null));
    }
}
