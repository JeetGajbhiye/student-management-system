package com.sms.studentmanagement.util;

import lombok.experimental.UtilityClass;

/**
 * Grade computation utilities (percentage → letter grade).
 */
@UtilityClass
public class GradeUtils {

    /**
     * Compute a letter grade from marks and max marks.
     * Scale: A+ ≥90, A ≥80, B ≥70, C ≥60, D ≥50, F <50.
     */
    public static String computeGradeLetter(double marksObtained, double maxMarks) {
        if (maxMarks <= 0) return "F";
        double pct = (marksObtained / maxMarks) * 100.0;
        if (pct >= 90) return "A+";
        if (pct >= 80) return "A";
        if (pct >= 70) return "B";
        if (pct >= 60) return "C";
        if (pct >= 50) return "D";
        return "F";
    }

    /**
     * Compute GPA on a 4.0 scale from a percentage.
     */
    public static double computeGpa(double percentage) {
        if (percentage >= 90) return 4.0;
        if (percentage >= 80) return 3.5;
        if (percentage >= 70) return 3.0;
        if (percentage >= 60) return 2.5;
        if (percentage >= 50) return 2.0;
        return 0.0;
    }

    /**
     * Returns a human-readable remark based on percentage.
     */
    public static String computeRemark(double percentage) {
        if (percentage >= 90) return "Outstanding";
        if (percentage >= 80) return "Excellent";
        if (percentage >= 70) return "Good";
        if (percentage >= 60) return "Average";
        if (percentage >= 50) return "Satisfactory";
        return "Needs Improvement";
    }
}
