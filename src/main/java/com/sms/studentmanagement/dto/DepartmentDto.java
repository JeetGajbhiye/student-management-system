package com.sms.studentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

public class DepartmentDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Department name is required")
        @Size(max = 100, message = "Name must not exceed 100 characters")
        private String name;

        @NotBlank(message = "Department code is required")
        @Size(max = 20, message = "Code must not exceed 20 characters")
        private String code;

        @Size(max = 500, message = "Description must not exceed 500 characters")
        private String description;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String name;
        private String code;
        private String description;
        private int totalStudents;
        private int totalCourses;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
