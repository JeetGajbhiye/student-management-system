-- ============================================================
-- V1__create_schema.sql
-- Initial schema creation for Student Management System
-- ============================================================

CREATE TABLE IF NOT EXISTS roles (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name  VARCHAR(50),
    enabled    BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at DATETIME(6)  NOT NULL,
    updated_at DATETIME(6)  NOT NULL,
    created_by VARCHAR(50),
    updated_by VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS departments (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    code        VARCHAR(20)  NOT NULL UNIQUE,
    description VARCHAR(500),
    created_at  DATETIME(6)  NOT NULL,
    updated_at  DATETIME(6)  NOT NULL,
    created_by  VARCHAR(50),
    updated_by  VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS students (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name      VARCHAR(50)  NOT NULL,
    last_name       VARCHAR(50)  NOT NULL,
    email           VARCHAR(100) NOT NULL UNIQUE,
    phone           VARCHAR(15),
    date_of_birth   DATE,
    gender          VARCHAR(10),
    address         VARCHAR(300),
    enrollment_date DATE,
    student_id      VARCHAR(20)  UNIQUE,
    department_id   BIGINT,
    created_at      DATETIME(6)  NOT NULL,
    updated_at      DATETIME(6)  NOT NULL,
    created_by      VARCHAR(50),
    updated_by      VARCHAR(50),
    CONSTRAINT fk_stu_dept FOREIGN KEY (department_id) REFERENCES departments (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS courses (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    title         VARCHAR(150) NOT NULL,
    code          VARCHAR(20)  NOT NULL UNIQUE,
    description   VARCHAR(500),
    credits       INT          NOT NULL,
    department_id BIGINT,
    created_at    DATETIME(6)  NOT NULL,
    updated_at    DATETIME(6)  NOT NULL,
    created_by    VARCHAR(50),
    updated_by    VARCHAR(50),
    CONSTRAINT fk_course_dept FOREIGN KEY (department_id) REFERENCES departments (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS enrollments (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id      BIGINT      NOT NULL,
    course_id       BIGINT      NOT NULL,
    enrollment_date DATE        NOT NULL,
    status          VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at      DATETIME(6) NOT NULL,
    updated_at      DATETIME(6) NOT NULL,
    created_by      VARCHAR(50),
    updated_by      VARCHAR(50),
    CONSTRAINT uq_enroll UNIQUE (student_id, course_id),
    CONSTRAINT fk_enroll_stu  FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
    CONSTRAINT fk_enroll_crs  FOREIGN KEY (course_id)  REFERENCES courses  (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS grades (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    enrollment_id   BIGINT         NOT NULL UNIQUE,
    marks_obtained  DECIMAL(6, 2)  NOT NULL,
    max_marks       DECIMAL(6, 2)  NOT NULL,
    grade_letter    VARCHAR(5),
    remarks         VARCHAR(300),
    exam_date       DATE,
    created_at      DATETIME(6)    NOT NULL,
    updated_at      DATETIME(6)    NOT NULL,
    created_by      VARCHAR(50),
    updated_by      VARCHAR(50),
    CONSTRAINT fk_grade_enroll FOREIGN KEY (enrollment_id) REFERENCES enrollments (id) ON DELETE CASCADE
);

-- Indexes for common query patterns
CREATE INDEX idx_students_email       ON students   (email);
CREATE INDEX idx_students_dept        ON students   (department_id);
CREATE INDEX idx_courses_dept         ON courses    (department_id);
CREATE INDEX idx_enrollments_student  ON enrollments(student_id);
CREATE INDEX idx_enrollments_course   ON enrollments(course_id);
CREATE INDEX idx_enrollments_status   ON enrollments(status);
CREATE INDEX idx_grades_enrollment    ON grades     (enrollment_id);
