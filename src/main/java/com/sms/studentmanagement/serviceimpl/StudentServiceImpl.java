package com.sms.studentmanagement.serviceimpl;

import com.sms.studentmanagement.dto.PagedResponse;
import com.sms.studentmanagement.dto.StudentDto;
import com.sms.studentmanagement.entity.Department;
import com.sms.studentmanagement.entity.Student;
import com.sms.studentmanagement.exception.DuplicateResourceException;
import com.sms.studentmanagement.exception.ResourceNotFoundException;
import com.sms.studentmanagement.repository.DepartmentRepository;
import com.sms.studentmanagement.repository.StudentRepository;
import com.sms.studentmanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public StudentDto.Response createStudent(StudentDto.Request request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Student", "email", request.getEmail());
        }

        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
        }

        Student student = Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .address(request.getAddress())
                .enrollmentDate(request.getEnrollmentDate() != null ? request.getEnrollmentDate() : LocalDate.now())
                .department(department)
                .build();

        student.setStudentId(generateStudentId());
        Student saved = studentRepository.save(student);
        log.info("Student created: {} - {}", saved.getStudentId(), saved.getEmail());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDto.Response getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        return toResponse(student);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDto.Response getStudentByStudentId(String studentId) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "studentId", studentId));
        return toResponse(student);
    }

    @Override
    @Transactional
    public StudentDto.Response updateStudent(Long id, StudentDto.Request request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        if (!student.getEmail().equals(request.getEmail()) &&
                studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Student", "email", request.getEmail());
        }

        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
        }

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setGender(request.getGender());
        student.setAddress(request.getAddress());
        student.setEnrollmentDate(request.getEnrollmentDate());
        student.setDepartment(department);

        Student updated = studentRepository.save(student);
        log.info("Student updated: {}", updated.getStudentId());
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        studentRepository.delete(student);
        log.info("Student deleted: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<StudentDto.Response> getAllStudents(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Student> students = studentRepository.findAll(pageable);
        return buildPagedResponse(students);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<StudentDto.Response> searchStudents(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Student> students = studentRepository.searchStudents(keyword, pageable);
        return buildPagedResponse(students);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<StudentDto.Response> getStudentsByDepartment(Long departmentId, int page, int size) {
        departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));
        Pageable pageable = PageRequest.of(page, size);
        Page<Student> students = studentRepository.findByDepartmentId(departmentId, pageable);
        return buildPagedResponse(students);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto.Response> getAllStudentsAsList() {
        return studentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private String generateStudentId() {
        String year = String.valueOf(LocalDate.now().getYear());
        String prefix = "STU" + year;
        return studentRepository.findMaxStudentIdByPrefix(prefix)
                .map(lastId -> {
                    int lastNum = Integer.parseInt(lastId.substring(prefix.length()));
                    return prefix + String.format("%03d", lastNum + 1);
                })
                .orElse(prefix + "001");
    }

    private PagedResponse<StudentDto.Response> buildPagedResponse(Page<Student> students) {
        List<StudentDto.Response> content = students.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return PagedResponse.<StudentDto.Response>builder()
                .content(content)
                .pageNumber(students.getNumber())
                .pageSize(students.getSize())
                .totalElements(students.getTotalElements())
                .totalPages(students.getTotalPages())
                .last(students.isLast())
                .build();
    }

    private StudentDto.Response toResponse(Student s) {
        return StudentDto.Response.builder()
                .id(s.getId())
                .firstName(s.getFirstName())
                .lastName(s.getLastName())
                .email(s.getEmail())
                .phone(s.getPhone())
                .dateOfBirth(s.getDateOfBirth())
                .gender(s.getGender())
                .address(s.getAddress())
                .enrollmentDate(s.getEnrollmentDate())
                .studentId(s.getStudentId())
                .departmentId(s.getDepartment() != null ? s.getDepartment().getId() : null)
                .departmentName(s.getDepartment() != null ? s.getDepartment().getName() : null)
                .totalEnrollments(s.getEnrollments() != null ? s.getEnrollments().size() : 0)
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}
