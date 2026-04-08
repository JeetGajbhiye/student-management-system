package com.sms.studentmanagement.constants;

/**
 * Centralised error message constants so strings are never duplicated.
 */
public final class ErrorMessages {

    private ErrorMessages() {}

    // Generic
    public static final String RESOURCE_NOT_FOUND       = "%s not found with %s: '%s'";
    public static final String RESOURCE_ALREADY_EXISTS  = "%s already exists with %s: '%s'";
    public static final String OPERATION_NOT_ALLOWED    = "Operation not allowed: %s";

    // Student
    public static final String STUDENT_NOT_FOUND        = "Student not found with id: %s";
    public static final String STUDENT_EMAIL_DUPLICATE  = "A student with email '%s' already exists";
    public static final String STUDENT_ID_DUPLICATE     = "Student ID '%s' is already assigned";

    // Department
    public static final String DEPT_NOT_FOUND           = "Department not found with id: %s";
    public static final String DEPT_CODE_DUPLICATE      = "Department code '%s' is already used";
    public static final String DEPT_HAS_STUDENTS        = "Cannot delete department: it still has enrolled students";
    public static final String DEPT_HAS_COURSES         = "Cannot delete department: it still has associated courses";

    // Course
    public static final String COURSE_NOT_FOUND         = "Course not found with id: %s";
    public static final String COURSE_CODE_DUPLICATE    = "Course code '%s' is already used";

    // Enrollment
    public static final String ENROLLMENT_NOT_FOUND     = "Enrollment not found with id: %s";
    public static final String ALREADY_ENROLLED         = "Student is already enrolled in this course";

    // Grade
    public static final String GRADE_NOT_FOUND          = "Grade not found with id: %s";
    public static final String GRADE_ALREADY_ASSIGNED   = "A grade already exists for this enrollment. Use PUT to update.";
    public static final String MARKS_EXCEED_MAX         = "Marks obtained (%s) cannot exceed max marks (%s)";

    // Auth
    public static final String USER_NOT_FOUND           = "User not found with username: %s";
    public static final String USERNAME_TAKEN           = "Username '%s' is already taken";
    public static final String EMAIL_TAKEN              = "Email '%s' is already registered";
    public static final String ROLE_NOT_FOUND           = "Role not found: %s";
    public static final String INVALID_CREDENTIALS      = "Invalid username or password";
    public static final String INVALID_TOKEN            = "Invalid or expired token";
}
