package com.sms.studentmanagement.service;

import com.sms.studentmanagement.dto.StudentDto;
import com.sms.studentmanagement.entity.Department;
import com.sms.studentmanagement.entity.Student;
import com.sms.studentmanagement.exception.DuplicateResourceException;
import com.sms.studentmanagement.exception.ResourceNotFoundException;
import com.sms.studentmanagement.repository.DepartmentRepository;
import com.sms.studentmanagement.repository.StudentRepository;
import com.sms.studentmanagement.serviceimpl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudentService Unit Tests")
class StudentServiceTest {

    @Mock private StudentRepository studentRepository;
    @Mock private DepartmentRepository departmentRepository;
    @InjectMocks private StudentServiceImpl studentService;

    private Department department;
    private Student student;
    private StudentDto.Request request;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("Computer Science");
        department.setCode("CS");

        student = Student.builder()
                .id(1L)
                .firstName("Alice")
                .lastName("Johnson")
                .email("alice@test.com")
                .studentId("STU2024001")
                .department(department)
                .enrollmentDate(LocalDate.now())
                .build();

        request = StudentDto.Request.builder()
                .firstName("Alice")
                .lastName("Johnson")
                .email("alice@test.com")
                .departmentId(1L)
                .enrollmentDate(LocalDate.now())
                .build();
    }

    @Test
    @DisplayName("Create student - success")
    void createStudent_success() {
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(studentRepository.findMaxStudentIdByPrefix(anyString())).thenReturn(Optional.empty());
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentDto.Response response = studentService.createStudent(request);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("alice@test.com");
        assertThat(response.getFirstName()).isEqualTo("Alice");
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    @DisplayName("Create student - duplicate email throws exception")
    void createStudent_duplicateEmail_throwsException() {
        when(studentRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> studentService.createStudent(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("email");
        verify(studentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Get student by ID - success")
    void getStudentById_success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentDto.Response response = studentService.getStudentById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Get student by ID - not found throws exception")
    void getStudentById_notFound_throwsException() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Get all students - returns paged response")
    void getAllStudents_returnsPagedResponse() {
        Page<Student> page = new PageImpl<>(List.of(student), PageRequest.of(0, 10), 1);
        when(studentRepository.findAll(any(Pageable.class))).thenReturn(page);

        var result = studentService.getAllStudents(0, 10, "firstName", "asc");

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Delete student - success")
    void deleteStudent_success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).delete(student);

        assertThatCode(() -> studentService.deleteStudent(1L)).doesNotThrowAnyException();
        verify(studentRepository).delete(student);
    }

    @Test
    @DisplayName("Update student - success")
    void updateStudent_success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentDto.Response response = studentService.updateStudent(1L, request);

        assertThat(response).isNotNull();
        verify(studentRepository).save(any(Student.class));
    }
}
