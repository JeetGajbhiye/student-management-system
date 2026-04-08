package com.sms.studentmanagement.serviceimpl;

import com.sms.studentmanagement.dto.DepartmentDto;
import com.sms.studentmanagement.entity.Department;
import com.sms.studentmanagement.exception.BadRequestException;
import com.sms.studentmanagement.exception.DuplicateResourceException;
import com.sms.studentmanagement.exception.ResourceNotFoundException;
import com.sms.studentmanagement.repository.DepartmentRepository;
import com.sms.studentmanagement.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public DepartmentDto.Response createDepartment(DepartmentDto.Request request) {
        if (departmentRepository.existsByCode(request.getCode().toUpperCase())) {
            throw new DuplicateResourceException("Department", "code", request.getCode());
        }
        if (departmentRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Department", "name", request.getName());
        }

        Department department = Department.builder()
                .name(request.getName())
                .code(request.getCode().toUpperCase())
                .description(request.getDescription())
                .build();

        Department saved = departmentRepository.save(department);
        log.info("Department created: {}", saved.getCode());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDto.Response getDepartmentById(Long id) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        return toResponse(dept);
    }

    @Override
    @Transactional
    public DepartmentDto.Response updateDepartment(Long id, DepartmentDto.Request request) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        String newCode = request.getCode().toUpperCase();
        if (!dept.getCode().equals(newCode) && departmentRepository.existsByCode(newCode)) {
            throw new DuplicateResourceException("Department", "code", newCode);
        }
        if (!dept.getName().equals(request.getName()) && departmentRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Department", "name", request.getName());
        }

        dept.setName(request.getName());
        dept.setCode(newCode);
        dept.setDescription(request.getDescription());

        Department updated = departmentRepository.save(dept);
        log.info("Department updated: {}", updated.getCode());
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        if (!dept.getStudents().isEmpty()) {
            throw new BadRequestException("Cannot delete department with enrolled students");
        }
        if (!dept.getCourses().isEmpty()) {
            throw new BadRequestException("Cannot delete department with associated courses");
        }

        departmentRepository.delete(dept);
        log.info("Department deleted: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDto.Response> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private DepartmentDto.Response toResponse(Department dept) {
        return DepartmentDto.Response.builder()
                .id(dept.getId())
                .name(dept.getName())
                .code(dept.getCode())
                .description(dept.getDescription())
                .totalStudents(dept.getStudents() != null ? dept.getStudents().size() : 0)
                .totalCourses(dept.getCourses() != null ? dept.getCourses().size() : 0)
                .createdAt(dept.getCreatedAt())
                .updatedAt(dept.getUpdatedAt())
                .build();
    }
}
