-- ============================================================
-- data.sql  –  H2 dev profile seed data only
-- For MySQL production, Flyway V2__seed_data.sql is used instead
-- ============================================================

-- Roles
INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_TEACHER');
INSERT INTO roles (id, name) VALUES (3, 'ROLE_STUDENT');

-- Users  (BCrypt of 'password')
INSERT INTO users (id, username, email, password, first_name, last_name, enabled, created_at, updated_at, created_by, updated_by)
VALUES (1, 'admin', 'admin@sms.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'System', 'Admin', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

INSERT INTO users (id, username, email, password, first_name, last_name, enabled, created_at, updated_at, created_by, updated_by)
VALUES (2, 'teacher1', 'teacher1@sms.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'John', 'Smith', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- User roles
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);

-- Departments
INSERT INTO departments (id, name, code, description, created_at, updated_at, created_by, updated_by)
VALUES (1, 'Computer Science', 'CS', 'Department of Computer Science and Engineering', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

INSERT INTO departments (id, name, code, description, created_at, updated_at, created_by, updated_by)
VALUES (2, 'Mathematics', 'MATH', 'Department of Mathematics and Statistics', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

INSERT INTO departments (id, name, code, description, created_at, updated_at, created_by, updated_by)
VALUES (3, 'Physics', 'PHY', 'Department of Physics', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

INSERT INTO departments (id, name, code, description, created_at, updated_at, created_by, updated_by)
VALUES (4, 'Electronics Engineering', 'ECE', 'Department of Electronics and Communication', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Courses
INSERT INTO courses (id, title, code, description, credits, department_id, created_at, updated_at, created_by, updated_by)
VALUES (1, 'Introduction to Programming', 'CS101', 'Fundamentals of programming using Java', 4, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

INSERT INTO courses (id, title, code, description, credits, department_id, created_at, updated_at, created_by, updated_by)
VALUES (2, 'Data Structures', 'CS201', 'Study of data structures and algorithms', 4, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

INSERT INTO courses (id, title, code, description, credits, department_id, created_at, updated_at, created_by, updated_by)
VALUES (3, 'Database Management Systems', 'CS301', 'Relational databases, SQL, and query design', 3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

INSERT INTO courses (id, title, code, description, credits, department_id, created_at, updated_at, created_by, updated_by)
VALUES (4, 'Calculus I', 'MATH101', 'Introduction to differential calculus', 3, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

INSERT INTO courses (id, title, code, description, credits, department_id, created_at, updated_at, created_by, updated_by)
VALUES (5, 'Linear Algebra', 'MATH201', 'Vectors, matrices and linear transformations', 3, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

INSERT INTO courses (id, title, code, description, credits, department_id, created_at, updated_at, created_by, updated_by)
VALUES (6, 'Classical Mechanics', 'PHY101', 'Newtonian mechanics and motion', 3, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Students
INSERT INTO students (id, first_name, last_name, email, phone, date_of_birth, gender, address, enrollment_date, student_id, department_id, created_at, updated_at, created_by, updated_by)
VALUES (1, 'Alice', 'Johnson', 'alice@student.com', '9876543210', '2002-03-15', 'FEMALE', '123 Main St, Raipur', '2022-07-01', 'STU2022001', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

INSERT INTO students (id, first_name, last_name, email, phone, date_of_birth, gender, address, enrollment_date, student_id, department_id, created_at, updated_at, created_by, updated_by)
VALUES (2, 'Bob', 'Williams', 'bob@student.com', '9876543211', '2001-08-22', 'MALE', '456 Oak Ave, Raipur', '2022-07-01', 'STU2022002', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

INSERT INTO students (id, first_name, last_name, email, phone, date_of_birth, gender, address, enrollment_date, student_id, department_id, created_at, updated_at, created_by, updated_by)
VALUES (3, 'Carol', 'Davis', 'carol@student.com', '9876543212', '2003-01-10', 'FEMALE', '789 Pine Rd, Raipur', '2023-07-01', 'STU2023001', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

INSERT INTO students (id, first_name, last_name, email, phone, date_of_birth, gender, address, enrollment_date, student_id, department_id, created_at, updated_at, created_by, updated_by)
VALUES (4, 'David', 'Brown', 'david@student.com', '9876543213', '2002-11-30', 'MALE', '321 Elm Blvd, Raipur', '2022-07-01', 'STU2022003', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

INSERT INTO students (id, first_name, last_name, email, phone, date_of_birth, gender, address, enrollment_date, student_id, department_id, created_at, updated_at, created_by, updated_by)
VALUES (5, 'Eva', 'Martinez', 'eva@student.com', '9876543214', '2003-05-20', 'FEMALE', '654 Cedar Ln, Raipur', '2023-07-01', 'STU2023002', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Enrollments
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, status, created_at, updated_at, created_by, updated_by) VALUES (1,1,1,'2022-08-01','ACTIVE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'system','system');
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, status, created_at, updated_at, created_by, updated_by) VALUES (2,1,3,'2022-08-01','ACTIVE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'system','system');
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, status, created_at, updated_at, created_by, updated_by) VALUES (3,1,4,'2022-08-01','ACTIVE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'system','system');
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, status, created_at, updated_at, created_by, updated_by) VALUES (4,2,1,'2022-08-01','ACTIVE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'system','system');
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, status, created_at, updated_at, created_by, updated_by) VALUES (5,2,2,'2022-08-01','ACTIVE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'system','system');
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, status, created_at, updated_at, created_by, updated_by) VALUES (6,3,4,'2023-08-01','ACTIVE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'system','system');
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, status, created_at, updated_at, created_by, updated_by) VALUES (7,3,5,'2023-08-01','ACTIVE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'system','system');
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, status, created_at, updated_at, created_by, updated_by) VALUES (8,4,6,'2022-08-01','ACTIVE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'system','system');
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, status, created_at, updated_at, created_by, updated_by) VALUES (9,5,1,'2023-08-01','ACTIVE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'system','system');
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, status, created_at, updated_at, created_by, updated_by) VALUES (10,5,2,'2023-08-01','ACTIVE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'system','system');

-- Grades
INSERT INTO grades (id, enrollment_id, marks_obtained, max_marks, grade_letter, remarks, exam_date, created_at, updated_at, created_by, updated_by) VALUES (1,1,88.5,100,'A','Excellent','2022-11-20',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'system','system');
INSERT INTO grades (id, enrollment_id, marks_obtained, max_marks, grade_letter, remarks, exam_date, created_at, updated_at, created_by, updated_by) VALUES (2,2,72.0,100,'B','Good','2022-11-20',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'system','system');
INSERT INTO grades (id, enrollment_id, marks_obtained, max_marks, grade_letter, remarks, exam_date, created_at, updated_at, created_by, updated_by) VALUES (3,4,95.0,100,'A+','Outstanding','2022-11-20',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'system','system');
INSERT INTO grades (id, enrollment_id, marks_obtained, max_marks, grade_letter, remarks, exam_date, created_at, updated_at, created_by, updated_by) VALUES (4,5,78.5,100,'B','Good','2022-11-20',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'system','system');
INSERT INTO grades (id, enrollment_id, marks_obtained, max_marks, grade_letter, remarks, exam_date, created_at, updated_at, created_by, updated_by) VALUES (5,6,91.0,100,'A+','Outstanding','2023-11-20',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'system','system');
