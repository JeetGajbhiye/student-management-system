package com.sms.studentmanagement.service;

import com.sms.studentmanagement.dto.GradeDto;
import com.sms.studentmanagement.entity.*;
import com.sms.studentmanagement.exception.BadRequestException;
import com.sms.studentmanagement.exception.DuplicateResourceException;
import com.sms.studentmanagement.exception.ResourceNotFoundException;
import com.sms.studentmanagement.repository.EnrollmentRepository;
import com.sms.studentmanagement.repository.GradeRepository;
import com.sms.studentmanagement.serviceimpl.GradeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
@DisplayName("GradeService Unit Tests")
class GradeServiceTest {

    @Mock private GradeRepository gradeRepository;
    @Mock private EnrollmentRepository enrollmentRepository;
    @InjectMocks private GradeServiceImpl gradeService;

    private Enrollment enrollment;
    private Grade grade;
    private GradeDto.Request request;

    @BeforeEach
    void setUp() {
        Student student = Student.builder()
                .id(1L).firstName("Alice").lastName("Johnson")
                .email("alice@test.com").studentId("STU2024001").build();

        Course course = Course.builder()
                .id(1L).title("Java Programming").code("CS101").credits(4).build();

        enrollment = Enrollment.builder()
                .id(1L).student(student).course(course)
                .enrollmentDate(LocalDate.now())
                .status(Enrollment.EnrollmentStatus.ACTIVE).build();

        grade = Grade.builder()
                .id(1L).enrollment(enrollment)
                .marksObtained(85.0).maxMarks(100.0)
                .gradeLetter("A").examDate(LocalDate.now()).build();

        request = GradeDto.Request.builder()
                .enrollmentId(1L).marksObtained(85.0).maxMarks(100.0)
                .examDate(LocalDate.now()).build();
    }

    @Test
    @DisplayName("Assign grade - success")
    void assignGrade_success() {
        when(gradeRepository.existsByEnrollmentId(1L)).thenReturn(false);
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(gradeRepository.save(any(Grade.class))).thenReturn(grade);

        GradeDto.Response response = gradeService.assignGrade(request);

        assertThat(response).isNotNull();
        assertThat(response.getMarksObtained()).isEqualTo(85.0);
        assertThat(response.getGradeLetter()).isEqualTo("A");
    }

    @Test
    @DisplayName("Assign grade - already exists throws exception")
    void assignGrade_alreadyExists_throwsException() {
        when(gradeRepository.existsByEnrollmentId(1L)).thenReturn(true);

        assertThatThrownBy(() -> gradeService.assignGrade(request))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    @DisplayName("Assign grade - marks exceed max throws exception")
    void assignGrade_marksExceedMax_throwsException() {
        when(gradeRepository.existsByEnrollmentId(1L)).thenReturn(false);
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        request.setMarksObtained(110.0);
        request.setMaxMarks(100.0);

        assertThatThrownBy(() -> gradeService.assignGrade(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("cannot exceed max marks");
    }

    @Test
    @DisplayName("Get grades by student - returns list")
    void getGradesByStudent_returnsList() {
        when(gradeRepository.findByStudentId(1L)).thenReturn(List.of(grade));

        List<GradeDto.Response> result = gradeService.getGradesByStudent(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPercentage()).isEqualTo(85.0);
    }

    @Test
    @DisplayName("Grade letter computed correctly")
    void gradeLetter_computedCorrectly() {
        when(gradeRepository.existsByEnrollmentId(anyLong())).thenReturn(false);
        when(enrollmentRepository.findById(anyLong())).thenReturn(Optional.of(enrollment));

        // Test A+ (>=90)
        request.setMarksObtained(92.0);
        Grade gradeAPlus = Grade.builder().id(2L).enrollment(enrollment)
                .marksObtained(92.0).maxMarks(100.0).gradeLetter("A+").examDate(LocalDate.now()).build();
        when(gradeRepository.save(any(Grade.class))).thenReturn(gradeAPlus);
        GradeDto.Response resp = gradeService.assignGrade(request);
        assertThat(resp.getGradeLetter()).isEqualTo("A+");
    }

    @Test
    @DisplayName("Delete grade - not found throws exception")
    void deleteGrade_notFound_throwsException() {
        when(gradeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gradeService.deleteGrade(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
