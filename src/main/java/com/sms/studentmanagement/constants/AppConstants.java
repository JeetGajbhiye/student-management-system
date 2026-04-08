package com.sms.studentmanagement.constants;

/**
 * Application-wide constants.
 */
public final class AppConstants {

    private AppConstants() {}

    // ---------- Pagination defaults ----------
    public static final int DEFAULT_PAGE_NUMBER  = 0;
    public static final int DEFAULT_PAGE_SIZE    = 10;
    public static final int MAX_PAGE_SIZE        = 100;
    public static final String DEFAULT_SORT_BY   = "createdAt";
    public static final String DEFAULT_SORT_DIR  = "desc";

    // ---------- JWT ----------
    public static final String TOKEN_PREFIX      = "Bearer ";
    public static final String HEADER_STRING     = "Authorization";

    // ---------- Roles ----------
    public static final String ROLE_ADMIN        = "ROLE_ADMIN";
    public static final String ROLE_TEACHER      = "ROLE_TEACHER";
    public static final String ROLE_STUDENT      = "ROLE_STUDENT";

    // ---------- Student ID ----------
    public static final String STUDENT_ID_PREFIX = "STU";

    // ---------- Cache names ----------
    public static final String CACHE_DEPARTMENTS = "departments";
    public static final String CACHE_COURSES     = "courses";
    public static final String CACHE_STUDENTS    = "students";
    public static final String CACHE_DASHBOARD   = "dashboard";

    // ---------- Date formats ----------
    public static final String DATE_FORMAT       = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT   = "yyyy-MM-dd'T'HH:mm:ss";

    // ---------- Regex ----------
    public static final String PHONE_REGEX       = "^[0-9]{10}$";
    public static final String EMAIL_REGEX       = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    // ---------- Logging markers ----------
    public static final String LOG_ENTRY         = ">> Entering {}";
    public static final String LOG_EXIT          = "<< Exiting  {}";
}
