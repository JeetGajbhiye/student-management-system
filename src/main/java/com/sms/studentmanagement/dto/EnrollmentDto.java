package com.sms.studentmanagement.dto;

import com.sms.studentmanagement.entity.Enrollment.EnrollmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EnrollmentDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotNull(message = "Student ID is required")
        private Long studentId;

        @NotNull(message = "Course ID is required")
        private Long courseId;

        private LocalDate enrollmentDate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long studentId;
        private String studentName;
        private String studentIdNumber;
        private Long courseId;
        private String courseTitle;
        private String courseCode;
        private LocalDate enrollmentDate;
        private EnrollmentStatus status;
        private GradeDto.Response grade;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StatusUpdateRequest {
        @NotNull(message = "Status is required")
        private EnrollmentStatus status;
    }
}
