package com.sms.studentmanagement.util;

import com.sms.studentmanagement.constants.AppConstants;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * Date/time utility methods.
 */
@UtilityClass
public class DateUtils {

    private static final DateTimeFormatter DATE_FMT     = DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT);
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern(AppConstants.DATETIME_FORMAT);

    public static String formatDate(LocalDate date) {
        return date == null ? null : date.format(DATE_FMT);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DATETIME_FMT);
    }

    public static LocalDate parseDate(String date) {
        return (date == null || date.isBlank()) ? null : LocalDate.parse(date, DATE_FMT);
    }

    /**
     * Returns the age in years from a date of birth to today.
     */
    public static int calculateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) return 0;
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    /**
     * Returns the current academic year string, e.g. "2024-25".
     */
    public static String currentAcademicYear() {
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        // Academic year starts in July
        int startYear = month >= 7 ? year : year - 1;
        return startYear + "-" + String.valueOf(startYear + 1).substring(2);
    }
}
