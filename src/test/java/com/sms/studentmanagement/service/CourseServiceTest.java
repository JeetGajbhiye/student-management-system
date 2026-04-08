package com.sms.studentmanagement.service;

import com.sms.studentmanagement.dto.CourseDto;
import com.sms.studentmanagement.dto.PagedResponse;
import com.sms.studentmanagement.entity.Course;
import com.sms.studentmanagement.entity.Department;
import com.sms.studentmanagement.exception.DuplicateResourceException;
import com.sms.studentmanagement.exception.ResourceNotFoundException;
import com.sms.studentmanagement.repository.CourseRepository;
import com.sms.studentmanagement.repository.DepartmentRepository;
import com.sms.studentmanagement.serviceimpl.CourseServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseService Unit Tests")
class CourseServiceTest {

    @Mock private CourseRepository courseRepository;
    @Mock private DepartmentRepository departmentRepository;
    @InjectMocks private CourseServiceImpl courseService;

    private Department department;
    private Course course;
    private CourseDto.Request request;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L); department.setName("CS"); department.setCode("CS");
        department.setStudents(new ArrayList<>());
        department.setCourses(new ArrayList<>());

        course = Course.builder()
                .id(1L).title("Java Programming").code("CS101")
                .credits(4).department(department).enrollments(new ArrayList<>()).build();

        request = CourseDto.Request.builder()
                .title("Java Programming").code("CS101")
                .credits(4).departmentId(1L).build();
    }

    @Test
    @DisplayName("Create course - success")
    void createCourse_success() {
        when(courseRepository.existsByCode(anyString())).thenReturn(false);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        CourseDto.Response response = courseService.createCourse(request);

        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo("CS101");
        assertThat(response.getCredits()).isEqualTo(4);
    }

    @Test
    @DisplayName("Create course - duplicate code throws exception")
    void createCourse_duplicateCode_throwsException() {
        when(courseRepository.existsByCode(anyString())).thenReturn(true);

        assertThatThrownBy(() -> courseService.createCourse(request))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    @DisplayName("Get course by ID - success")
    void getCourseById_success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        CourseDto.Response response = courseService.getCourseById(1L);
        assertThat(response.getTitle()).isEqualTo("Java Programming");
    }

    @Test
    @DisplayName("Get course by ID - not found throws exception")
    void getCourseById_notFound_throwsException() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.getCourseById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Get all courses - returns paged response")
    void getAllCourses_returnsPagedResponse() {
        Page<Course> page = new PageImpl<>(List.of(course), PageRequest.of(0, 10), 1);
        when(courseRepository.findAll(any(Pageable.class))).thenReturn(page);

        PagedResponse<CourseDto.Response> result = courseService.getAllCourses(0, 10, "title", "asc");

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Get courses by department - returns list")
    void getCoursesByDepartment_returnsList() {
        when(courseRepository.findByDepartmentId(1L)).thenReturn(List.of(course));

        List<CourseDto.Response> result = courseService.getCoursesByDepartment(1L);
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Delete course - success")
    void deleteCourse_success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).delete(course);

        assertThatCode(() -> courseService.deleteCourse(1L)).doesNotThrowAnyException();
        verify(courseRepository).delete(course);
    }
}
