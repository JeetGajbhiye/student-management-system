package com.sms.studentmanagement.repository;

import com.sms.studentmanagement.entity.Department;
import com.sms.studentmanagement.entity.Student;
import com.sms.studentmanagement.entity.Student.Gender;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("StudentRepository JPA Tests")
class StudentRepositoryTest {

    @Autowired private StudentRepository studentRepository;
    @Autowired private DepartmentRepository departmentRepository;

    private Department department;
    private Student student;

    @BeforeEach
    void setUp() {
        department = departmentRepository.save(
                Department.builder().name("Computer Science").code("CS").build());

        student = studentRepository.save(Student.builder()
                .firstName("Alice").lastName("Johnson")
                .email("alice@test.com").phone("9876543210")
                .gender(Gender.FEMALE).dateOfBirth(LocalDate.of(2002, 3, 15))
                .enrollmentDate(LocalDate.now()).studentId("STU2024001")
                .department(department).build());
    }

    @Test
    @DisplayName("findByEmail - returns student")
    void findByEmail_returnsStudent() {
        Optional<Student> found = studentRepository.findByEmail("alice@test.com");
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Alice");
    }

    @Test
    @DisplayName("findByEmail - not found returns empty")
    void findByEmail_notFound_returnsEmpty() {
        Optional<Student> found = studentRepository.findByEmail("notexist@test.com");
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("findByStudentId - returns student")
    void findByStudentId_returnsStudent() {
        Optional<Student> found = studentRepository.findByStudentId("STU2024001");
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("existsByEmail - returns true when exists")
    void existsByEmail_returnsTrue() {
        assertThat(studentRepository.existsByEmail("alice@test.com")).isTrue();
    }

    @Test
    @DisplayName("existsByEmail - returns false when not exists")
    void existsByEmail_returnsFalse() {
        assertThat(studentRepository.existsByEmail("nobody@test.com")).isFalse();
    }

    @Test
    @DisplayName("searchStudents - matches on firstName")
    void searchStudents_matchesFirstName() {
        Page<Student> result = studentRepository.searchStudents("alice", PageRequest.of(0, 10));
        assertThat(result.getTotalElements()).isGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("searchStudents - matches on email")
    void searchStudents_matchesEmail() {
        Page<Student> result = studentRepository.searchStudents("alice@test", PageRequest.of(0, 10));
        assertThat(result.getTotalElements()).isGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("searchStudents - no match returns empty page")
    void searchStudents_noMatch_returnsEmpty() {
        Page<Student> result = studentRepository.searchStudents("zzznomatch", PageRequest.of(0, 10));
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("findByDepartmentId - returns paged students")
    void findByDepartmentId_returnsStudents() {
        Page<Student> result = studentRepository.findByDepartmentId(department.getId(), PageRequest.of(0, 10));
        assertThat(result.getTotalElements()).isGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("countByDepartmentId - returns correct count")
    void countByDepartmentId_returnsCorrectCount() {
        long count = studentRepository.countByDepartmentId(department.getId());
        assertThat(count).isEqualTo(1);
    }

    @AfterEach
    void tearDown() {
        studentRepository.deleteAll();
        departmentRepository.deleteAll();
    }
}
