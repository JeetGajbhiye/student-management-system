package com.sms.studentmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "grades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false, unique = true)
    private Enrollment enrollment;

    @Column(name = "marks_obtained", nullable = false)
    private Double marksObtained;

    @Column(name = "max_marks", nullable = false)
    private Double maxMarks;

    @Column(name = "grade_letter", length = 5)
    private String gradeLetter;

    @Column(length = 300)
    private String remarks;

    @Column(name = "exam_date")
    private LocalDate examDate;

    public Double getPercentage() {
        if (maxMarks == null || maxMarks == 0) return 0.0;
        return (marksObtained / maxMarks) * 100;
    }
}
