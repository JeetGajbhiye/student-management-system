package com.sms.studentmanagement.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

public class CourseDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Course title is required")
        @Size(max = 150, message = "Title must not exceed 150 characters")
        private String title;

        @NotBlank(message = "Course code is required")
        @Size(max = 20, message = "Code must not exceed 20 characters")
        private String code;

        @Size(max = 500, message = "Description must not exceed 500 characters")
        private String description;

        @NotNull(message = "Credits are required")
        @Min(value = 1, message = "Credits must be at least 1")
        @Max(value = 10, message = "Credits must not exceed 10")
        private Integer credits;

        @NotNull(message = "Department ID is required")
        private Long departmentId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String title;
        private String code;
        private String description;
        private Integer credits;
        private Long departmentId;
        private String departmentName;
        private int totalEnrollments;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
