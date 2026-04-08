package com.sms.studentmanagement.service;

import com.sms.studentmanagement.dto.EnrollmentDto;
import com.sms.studentmanagement.entity.*;
import com.sms.studentmanagement.entity.Enrollment.EnrollmentStatus;
import com.sms.studentmanagement.exception.DuplicateResourceException;
import com.sms.studentmanagement.exception.ResourceNotFoundException;
import com.sms.studentmanagement.repository.CourseRepository;
import com.sms.studentmanagement.repository.EnrollmentRepository;
import com.sms.studentmanagement.repository.StudentRepository;
import com.sms.studentmanagement.serviceimpl.EnrollmentServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EnrollmentService Unit Tests")
class EnrollmentServiceTest {

    @Mock private EnrollmentRepository enrollmentRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private CourseRepository courseRepository;
    @InjectMocks private EnrollmentServiceImpl enrollmentService;

    private Student student;
    private Course course;
    private Enrollment enrollment;
    private EnrollmentDto.Request request;

    @BeforeEach
    void setUp() {
        Department dept = new Department();
        dept.setId(1L); dept.setName("CS"); dept.setCode("CS");

        student = Student.builder()
                .id(1L).firstName("Alice").lastName("Johnson")
                .email("alice@test.com").studentId("STU2024001")
                .department(dept).build();

        course = Course.builder()
                .id(1L).title("Java Programming").code("CS101")
                .credits(4).department(dept).build();

        enrollment = Enrollment.builder()
                .id(1L).student(student).course(course)
                .enrollmentDate(LocalDate.now()).status(EnrollmentStatus.ACTIVE).build();

        request = EnrollmentDto.Request.builder()
                .studentId(1L).courseId(1L).enrollmentDate(LocalDate.now()).build();
    }

    @Test
    @DisplayName("Enroll student - success")
    void enrollStudent_success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(false);
        when(enrollmentRepository.save(any())).thenReturn(enrollment);

        EnrollmentDto.Response response = enrollmentService.enrollStudent(request);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(EnrollmentStatus.ACTIVE);
        assertThat(response.getStudentName()).isEqualTo("Alice Johnson");
        assertThat(response.getCourseCode()).isEqualTo("CS101");
    }

    @Test
    @DisplayName("Enroll student - already enrolled throws exception")
    void enrollStudent_alreadyEnrolled_throwsException() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> enrollmentService.enrollStudent(request))
                .isInstanceOf(DuplicateResourceException.class);
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Enroll student - student not found throws exception")
    void enrollStudent_studentNotFound_throwsException() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.enrollStudent(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Update enrollment status - success")
    void updateEnrollmentStatus_success() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        when(enrollmentRepository.save(any())).thenReturn(enrollment);

        EnrollmentDto.StatusUpdateRequest statusReq = new EnrollmentDto.StatusUpdateRequest(EnrollmentStatus.COMPLETED);
        EnrollmentDto.Response response = enrollmentService.updateEnrollmentStatus(1L, statusReq);

        assertThat(response.getStatus()).isEqualTo(EnrollmentStatus.COMPLETED);
    }

    @Test
    @DisplayName("Get enrollments by student - returns list")
    void getEnrollmentsByStudent_returnsList() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(enrollmentRepository.findByStudentIdWithDetails(1L)).thenReturn(List.of(enrollment));

        List<EnrollmentDto.Response> result = enrollmentService.getEnrollmentsByStudent(1L);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStudentId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Delete enrollment - success")
    void deleteEnrollment_success() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        doNothing().when(enrollmentRepository).delete(enrollment);

        assertThatCode(() -> enrollmentService.deleteEnrollment(1L)).doesNotThrowAnyException();
        verify(enrollmentRepository).delete(enrollment);
    }
}
