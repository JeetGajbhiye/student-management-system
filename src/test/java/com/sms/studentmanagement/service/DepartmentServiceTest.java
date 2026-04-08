package com.sms.studentmanagement.service;

import com.sms.studentmanagement.dto.DepartmentDto;
import com.sms.studentmanagement.entity.Department;
import com.sms.studentmanagement.exception.BadRequestException;
import com.sms.studentmanagement.exception.DuplicateResourceException;
import com.sms.studentmanagement.exception.ResourceNotFoundException;
import com.sms.studentmanagement.repository.DepartmentRepository;
import com.sms.studentmanagement.serviceimpl.DepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DepartmentService Unit Tests")
class DepartmentServiceTest {

    @Mock private DepartmentRepository departmentRepository;
    @InjectMocks private DepartmentServiceImpl departmentService;

    private Department department;
    private DepartmentDto.Request request;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("Computer Science");
        department.setCode("CS");
        department.setDescription("CS Department");
        department.setStudents(new ArrayList<>());
        department.setCourses(new ArrayList<>());

        request = DepartmentDto.Request.builder()
                .name("Computer Science")
                .code("CS")
                .description("CS Department")
                .build();
    }

    @Test
    @DisplayName("Create department - success")
    void createDepartment_success() {
        when(departmentRepository.existsByCode(anyString())).thenReturn(false);
        when(departmentRepository.existsByName(anyString())).thenReturn(false);
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        DepartmentDto.Response response = departmentService.createDepartment(request);

        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo("CS");
        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    @DisplayName("Create department - duplicate code throws exception")
    void createDepartment_duplicateCode_throwsException() {
        when(departmentRepository.existsByCode(anyString())).thenReturn(true);

        assertThatThrownBy(() -> departmentService.createDepartment(request))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    @DisplayName("Get all departments - returns list")
    void getAllDepartments_returnsList() {
        when(departmentRepository.findAll()).thenReturn(List.of(department));

        List<DepartmentDto.Response> result = departmentService.getAllDepartments();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Computer Science");
    }

    @Test
    @DisplayName("Delete department with students - throws exception")
    void deleteDepartment_withStudents_throwsException() {
        department.getStudents().add(new com.sms.studentmanagement.entity.Student());
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        assertThatThrownBy(() -> departmentService.deleteDepartment(1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("students");
    }

    @Test
    @DisplayName("Get department by ID - not found throws exception")
    void getDepartmentById_notFound_throwsException() {
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.getDepartmentById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
