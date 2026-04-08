-- ============================================================
-- V3__add_indexes_constraints.sql
-- Additional indexes for performance optimisation
-- ============================================================

-- Full-text style index on student name fields for search
CREATE INDEX idx_students_firstname ON students (first_name);
CREATE INDEX idx_students_lastname  ON students (last_name);
CREATE INDEX idx_students_studentid ON students (student_id);

-- Course title search
CREATE INDEX idx_courses_title ON courses (title);
CREATE INDEX idx_courses_code  ON courses (code);

-- Department code/name lookup
CREATE INDEX idx_departments_code ON departments (code);
CREATE INDEX idx_departments_name ON departments (name);

-- Enrollment date range queries
CREATE INDEX idx_enrollments_date   ON enrollments (enrollment_date);

-- Grade exam date
CREATE INDEX idx_grades_examdate    ON grades (exam_date);
