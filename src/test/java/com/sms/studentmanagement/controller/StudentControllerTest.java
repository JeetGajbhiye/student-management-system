package com.sms.studentmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.studentmanagement.config.JwtUtils;
import com.sms.studentmanagement.config.UserDetailsServiceImpl;
import com.sms.studentmanagement.dto.PagedResponse;
import com.sms.studentmanagement.dto.StudentDto;
import com.sms.studentmanagement.entity.Student.Gender;
import com.sms.studentmanagement.exception.ResourceNotFoundException;
import com.sms.studentmanagement.service.StudentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@DisplayName("StudentController Unit Tests")
class StudentControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private StudentService studentService;
    @MockBean private JwtUtils jwtUtils;
    @MockBean private UserDetailsServiceImpl userDetailsService;

    private StudentDto.Response buildSampleResponse() {
        return StudentDto.Response.builder()
                .id(1L)
                .firstName("Alice").lastName("Johnson")
                .email("alice@test.com")
                .studentId("STU2024001")
                .departmentId(1L).departmentName("Computer Science")
                .gender(Gender.FEMALE)
                .enrollmentDate(LocalDate.now())
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .build();
    }

    private StudentDto.Request buildSampleRequest() {
        return StudentDto.Request.builder()
                .firstName("Alice").lastName("Johnson")
                .email("alice@test.com")
                .departmentId(1L)
                .enrollmentDate(LocalDate.now())
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/students - creates student and returns 201")
    void createStudent_returnsCreated() throws Exception {
        when(studentService.createStudent(any())).thenReturn(buildSampleResponse());

        mockMvc.perform(post("/api/students")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildSampleRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("alice@test.com"))
                .andExpect(jsonPath("$.data.studentId").value("STU2024001"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    @DisplayName("GET /api/students/{id} - returns student")
    void getStudentById_returnsOk() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(buildSampleResponse());

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.firstName").value("Alice"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    @DisplayName("GET /api/students/{id} - not found returns 404")
    void getStudentById_notFound_returns404() throws Exception {
        when(studentService.getStudentById(99L)).thenThrow(new ResourceNotFoundException("Student", "id", 99L));

        mockMvc.perform(get("/api/students/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/students - blank firstName returns 400")
    void createStudent_blankFirstName_returns400() throws Exception {
        StudentDto.Request badReq = StudentDto.Request.builder()
                .firstName("").lastName("Test").email("test@test.com").build();

        mockMvc.perform(post("/api/students")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badReq)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/students - returns paged list")
    void getAllStudents_returnsPaged() throws Exception {
        PagedResponse<StudentDto.Response> paged = PagedResponse.<StudentDto.Response>builder()
                .content(List.of(buildSampleResponse()))
                .pageNumber(0).pageSize(10).totalElements(1).totalPages(1).last(true)
                .build();
        when(studentService.getAllStudents(anyInt(), anyInt(), anyString(), anyString())).thenReturn(paged);

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].email").value("alice@test.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /api/students/{id} - updates student")
    void updateStudent_returnsOk() throws Exception {
        when(studentService.updateStudent(eq(1L), any())).thenReturn(buildSampleResponse());

        mockMvc.perform(put("/api/students/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildSampleRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/students/{id} - deletes student")
    void deleteStudent_returnsOk() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/api/students/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("GET /api/students - no auth returns 401")
    void getAllStudents_noAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    @DisplayName("POST /api/students - STUDENT role returns 403")
    void createStudent_studentRole_returns403() throws Exception {
        mockMvc.perform(post("/api/students")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildSampleRequest())))
                .andExpect(status().isForbidden());
    }
}
