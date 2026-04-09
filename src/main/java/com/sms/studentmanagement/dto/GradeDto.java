package com.sms.studentmanagement.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class GradeDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotNull(message = "Enrollment ID is required")
        private Long enrollmentId;

        @NotNull(message = "Marks obtained is required")
        @DecimalMin(value = "0.0", message = "Marks cannot be negative")
        private BigDecimal  marksObtained;

        @NotNull(message = "Max marks is required")
        @DecimalMin(value = "1.0", message = "Max marks must be at least 1")
        private BigDecimal maxMarks;

        @Size(max = 5, message = "Grade letter must not exceed 5 characters")
        private String gradeLetter;

        @Size(max = 300, message = "Remarks must not exceed 300 characters")
        private String remarks;

        private LocalDate examDate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long enrollmentId;
        private String studentName;
        private String courseTitle;
        private Double marksObtained;
        private Double maxMarks;
        private Double percentage;
        private String gradeLetter;
        private String remarks;
        private LocalDate examDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
