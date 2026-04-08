package com.sms.studentmanagement.serviceimpl;

import com.sms.studentmanagement.dto.CourseDto;
import com.sms.studentmanagement.dto.PagedResponse;
import com.sms.studentmanagement.entity.Course;
import com.sms.studentmanagement.entity.Department;
import com.sms.studentmanagement.exception.DuplicateResourceException;
import com.sms.studentmanagement.exception.ResourceNotFoundException;
import com.sms.studentmanagement.repository.CourseRepository;
import com.sms.studentmanagement.repository.DepartmentRepository;
import com.sms.studentmanagement.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public CourseDto.Response createCourse(CourseDto.Request request) {
        if (courseRepository.existsByCode(request.getCode().toUpperCase())) {
            throw new DuplicateResourceException("Course", "code", request.getCode());
        }
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));

        Course course = Course.builder()
                .title(request.getTitle())
                .code(request.getCode().toUpperCase())
                .description(request.getDescription())
                .credits(request.getCredits())
                .department(department)
                .build();

        Course saved = courseRepository.save(course);
        log.info("Course created: {}", saved.getCode());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDto.Response getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        return toResponse(course);
    }

    @Override
    @Transactional
    public CourseDto.Response updateCourse(Long id, CourseDto.Request request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));

        String newCode = request.getCode().toUpperCase();
        if (!course.getCode().equals(newCode) && courseRepository.existsByCode(newCode)) {
            throw new DuplicateResourceException("Course", "code", newCode);
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));

        course.setTitle(request.getTitle());
        course.setCode(newCode);
        course.setDescription(request.getDescription());
        course.setCredits(request.getCredits());
        course.setDepartment(department);

        Course updated = courseRepository.save(course);
        log.info("Course updated: {}", updated.getCode());
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        courseRepository.delete(course);
        log.info("Course deleted: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<CourseDto.Response> getAllCourses(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Course> courses = courseRepository.findAll(pageable);
        List<CourseDto.Response> content = courses.getContent().stream().map(this::toResponse).collect(Collectors.toList());
        return PagedResponse.<CourseDto.Response>builder()
                .content(content).pageNumber(courses.getNumber()).pageSize(courses.getSize())
                .totalElements(courses.getTotalElements()).totalPages(courses.getTotalPages()).last(courses.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<CourseDto.Response> searchCourses(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Course> courses = courseRepository.searchCourses(keyword, pageable);
        List<CourseDto.Response> content = courses.getContent().stream().map(this::toResponse).collect(Collectors.toList());
        return PagedResponse.<CourseDto.Response>builder()
                .content(content).pageNumber(courses.getNumber()).pageSize(courses.getSize())
                .totalElements(courses.getTotalElements()).totalPages(courses.getTotalPages()).last(courses.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto.Response> getCoursesByDepartment(Long departmentId) {
        return courseRepository.findByDepartmentId(departmentId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    private CourseDto.Response toResponse(Course c) {
        return CourseDto.Response.builder()
                .id(c.getId())
                .title(c.getTitle())
                .code(c.getCode())
                .description(c.getDescription())
                .credits(c.getCredits())
                .departmentId(c.getDepartment() != null ? c.getDepartment().getId() : null)
                .departmentName(c.getDepartment() != null ? c.getDepartment().getName() : null)
                .totalEnrollments(c.getEnrollments() != null ? c.getEnrollments().size() : 0)
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
    }
}
