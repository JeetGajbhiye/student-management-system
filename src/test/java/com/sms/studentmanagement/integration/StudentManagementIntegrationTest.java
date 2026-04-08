package com.sms.studentmanagement.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.studentmanagement.dto.AuthDto;
import com.sms.studentmanagement.dto.DepartmentDto;
import com.sms.studentmanagement.dto.StudentDto;
import com.sms.studentmanagement.entity.Student.Gender;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("Full Integration Tests – H2 in-memory")
class StudentManagementIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private static String adminToken;
    private static Long deptId;
    private static Long studentId;

    // ─── Auth ────────────────────────────────────────────────

    @Test @Order(1)
    @DisplayName("Register admin")
    void registerAdmin() throws Exception {
        AuthDto.RegisterRequest req = AuthDto.RegisterRequest.builder()
                .username("itAdmin").email("it.admin@sms.com")
                .password("Admin@123").firstName("IT").lastName("Admin")
                .roles(Set.of("ADMIN")).build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test @Order(2)
    @DisplayName("Login and store token")
    void loginAdmin() throws Exception {
        AuthDto.LoginRequest req = new AuthDto.LoginRequest("itAdmin", "Admin@123");
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andReturn();
        adminToken = objectMapper.readTree(result.getResponse().getContentAsString())
                .path("data").path("accessToken").asText();
        assertThat(adminToken).isNotBlank();
    }

    @Test @Order(3)
    @DisplayName("Login with wrong password returns 401")
    void loginBadCreds_returns401() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"itAdmin\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }

    // ─── Departments ─────────────────────────────────────────

    @Test @Order(4)
    @DisplayName("Create department")
    void createDepartment() throws Exception {
        DepartmentDto.Request req = DepartmentDto.Request.builder()
                .name("IT Test Dept").code("ITD").description("Test").build();
        MvcResult result = mockMvc.perform(post("/api/departments")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.code").value("ITD"))
                .andReturn();
        deptId = objectMapper.readTree(result.getResponse().getContentAsString())
                .path("data").path("id").asLong();
        assertThat(deptId).isPositive();
    }

    @Test @Order(5)
    @DisplayName("Get all departments")
    void getAllDepartments_returnsArray() throws Exception {
        mockMvc.perform(get("/api/departments")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    // ─── Students ────────────────────────────────────────────

    @Test @Order(6)
    @DisplayName("Create student")
    void createStudent() throws Exception {
        StudentDto.Request req = StudentDto.Request.builder()
                .firstName("Priya").lastName("Sharma")
                .email("priya.sharma@test.com").phone("9876543299")
                .gender(Gender.FEMALE).dateOfBirth(LocalDate.of(2003, 4, 12))
                .enrollmentDate(LocalDate.now()).departmentId(deptId).build();
        MvcResult result = mockMvc.perform(post("/api/students")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.email").value("priya.sharma@test.com"))
                .andExpect(jsonPath("$.data.studentId").exists())
                .andReturn();
        studentId = objectMapper.readTree(result.getResponse().getContentAsString())
                .path("data").path("id").asLong();
        assertThat(studentId).isPositive();
    }

    @Test @Order(7)
    @DisplayName("Get student by ID")
    void getStudentById() throws Exception {
        mockMvc.perform(get("/api/students/{id}", studentId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName").value("Priya"));
    }

    @Test @Order(8)
    @DisplayName("Search students by keyword")
    void searchStudents() throws Exception {
        mockMvc.perform(get("/api/students/search")
                        .param("keyword", "Priya")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test @Order(9)
    @DisplayName("Duplicate email returns 409")
    void duplicateEmail_returns409() throws Exception {
        StudentDto.Request dup = StudentDto.Request.builder()
                .firstName("Dup").lastName("Test")
                .email("priya.sharma@test.com").departmentId(deptId).build();
        mockMvc.perform(post("/api/students")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dup)))
                .andExpect(status().isConflict());
    }

    @Test @Order(10)
    @DisplayName("Invalid student body returns 400")
    void invalidStudentBody_returns400() throws Exception {
        mockMvc.perform(post("/api/students")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test @Order(11)
    @DisplayName("No token returns 401")
    void noToken_returns401() throws Exception {
        mockMvc.perform(get("/api/students")).andExpect(status().isUnauthorized());
    }

    @Test @Order(12)
    @DisplayName("Delete student")
    void deleteStudent() throws Exception {
        mockMvc.perform(delete("/api/students/{id}", studentId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
