package com.sms.studentmanagement.dto;

import com.sms.studentmanagement.entity.Student.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StudentDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "First name is required")
        @Size(max = 50, message = "First name must not exceed 50 characters")
        private String firstName;

        @NotBlank(message = "Last name is required")
        @Size(max = 50, message = "Last name must not exceed 50 characters")
        private String lastName;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be a 10-digit number")
        private String phone;

        @Past(message = "Date of birth must be in the past")
        private LocalDate dateOfBirth;

        private Gender gender;

        @Size(max = 300, message = "Address must not exceed 300 characters")
        private String address;

        private LocalDate enrollmentDate;

        private Long departmentId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private LocalDate dateOfBirth;
        private Gender gender;
        private String address;
        private LocalDate enrollmentDate;
        private String studentId;
        private String departmentName;
        private Long departmentId;
        private int totalEnrollments;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
