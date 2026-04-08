-- ============================================================
-- V2__seed_data.sql
-- Initial seed data (roles, admin user, sample departments/courses/students)
-- ============================================================

-- Roles
INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN')   ON DUPLICATE KEY UPDATE name = name;
INSERT INTO roles (id, name) VALUES (2, 'ROLE_TEACHER')  ON DUPLICATE KEY UPDATE name = name;
INSERT INTO roles (id, name) VALUES (3, 'ROLE_STUDENT')  ON DUPLICATE KEY UPDATE name = name;

-- Users  (BCrypt of 'password' = $2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi)
INSERT INTO users (id, username, email, password, first_name, last_name, enabled, created_at, updated_at)
VALUES (1, 'admin',    'admin@sms.com',    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'System',  'Admin',   TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE username = username;

INSERT INTO users (id, username, email, password, first_name, last_name, enabled, created_at, updated_at)
VALUES (2, 'teacher1', 'teacher1@sms.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'John',    'Smith',   TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE username = username;

INSERT INTO users (id, username, email, password, first_name, last_name, enabled, created_at, updated_at)
VALUES (3, 'teacher2', 'teacher2@sms.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Sarah',   'Connor',  TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE username = username;

-- User ↔ Role
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1) ON DUPLICATE KEY UPDATE user_id = user_id;
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2) ON DUPLICATE KEY UPDATE user_id = user_id;
INSERT INTO user_roles (user_id, role_id) VALUES (3, 2) ON DUPLICATE KEY UPDATE user_id = user_id;

-- Departments
INSERT INTO departments (id, name, code, description, created_at, updated_at)
VALUES (1, 'Computer Science',       'CS',   'Department of Computer Science and Engineering', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = name;

INSERT INTO departments (id, name, code, description, created_at, updated_at)
VALUES (2, 'Mathematics',            'MATH', 'Department of Mathematics and Statistics',        NOW(), NOW())
ON DUPLICATE KEY UPDATE name = name;

INSERT INTO departments (id, name, code, description, created_at, updated_at)
VALUES (3, 'Physics',                'PHY',  'Department of Physics',                           NOW(), NOW())
ON DUPLICATE KEY UPDATE name = name;

INSERT INTO departments (id, name, code, description, created_at, updated_at)
VALUES (4, 'Electronics Engineering','ECE',  'Department of Electronics and Communication',    NOW(), NOW())
ON DUPLICATE KEY UPDATE name = name;

-- Courses
INSERT INTO courses (id, title, code, description, credits, department_id, created_at, updated_at)
VALUES (1, 'Introduction to Programming', 'CS101', 'Fundamentals of programming using Java',        4, 1, NOW(), NOW()) ON DUPLICATE KEY UPDATE code = code;
INSERT INTO courses (id, title, code, description, credits, department_id, created_at, updated_at)
VALUES (2, 'Data Structures',             'CS201', 'Study of data structures and algorithms',       4, 1, NOW(), NOW()) ON DUPLICATE KEY UPDATE code = code;
INSERT INTO courses (id, title, code, description, credits, department_id, created_at, updated_at)
VALUES (3, 'Database Management Systems', 'CS301', 'Relational databases, SQL, and query design',   3, 1, NOW(), NOW()) ON DUPLICATE KEY UPDATE code = code;
INSERT INTO courses (id, title, code, description, credits, department_id, created_at, updated_at)
VALUES (4, 'Operating Systems',           'CS401', 'Process management, memory, file systems',      3, 1, NOW(), NOW()) ON DUPLICATE KEY UPDATE code = code;
INSERT INTO courses (id, title, code, description, credits, department_id, created_at, updated_at)
VALUES (5, 'Calculus I',                  'MATH101','Introduction to differential calculus',         3, 2, NOW(), NOW()) ON DUPLICATE KEY UPDATE code = code;
INSERT INTO courses (id, title, code, description, credits, department_id, created_at, updated_at)
VALUES (6, 'Linear Algebra',              'MATH201','Vectors, matrices and linear transformations',  3, 2, NOW(), NOW()) ON DUPLICATE KEY UPDATE code = code;
INSERT INTO courses (id, title, code, description, credits, department_id, created_at, updated_at)
VALUES (7, 'Classical Mechanics',         'PHY101', 'Newtonian mechanics and motion',               3, 3, NOW(), NOW()) ON DUPLICATE KEY UPDATE code = code;

-- Students
INSERT INTO students (id, first_name, last_name, email, phone, date_of_birth, gender, address, enrollment_date, student_id, department_id, created_at, updated_at)
VALUES (1, 'Alice',   'Johnson', 'alice@student.com',   '9876543210', '2002-03-15', 'FEMALE', '123 Main St', '2022-07-01', 'STU2022001', 1, NOW(), NOW()) ON DUPLICATE KEY UPDATE student_id = student_id;
INSERT INTO students (id, first_name, last_name, email, phone, date_of_birth, gender, address, enrollment_date, student_id, department_id, created_at, updated_at)
VALUES (2, 'Bob',     'Williams','bob@student.com',     '9876543211', '2001-08-22', 'MALE',   '456 Oak Ave',  '2022-07-01', 'STU2022002', 1, NOW(), NOW()) ON DUPLICATE KEY UPDATE student_id = student_id;
INSERT INTO students (id, first_name, last_name, email, phone, date_of_birth, gender, address, enrollment_date, student_id, department_id, created_at, updated_at)
VALUES (3, 'Carol',   'Davis',   'carol@student.com',   '9876543212', '2003-01-10', 'FEMALE', '789 Pine Rd',  '2023-07-01', 'STU2023001', 2, NOW(), NOW()) ON DUPLICATE KEY UPDATE student_id = student_id;
INSERT INTO students (id, first_name, last_name, email, phone, date_of_birth, gender, address, enrollment_date, student_id, department_id, created_at, updated_at)
VALUES (4, 'David',   'Brown',   'david@student.com',   '9876543213', '2002-11-30', 'MALE',   '321 Elm Blvd', '2022-07-01', 'STU2022003', 3, NOW(), NOW()) ON DUPLICATE KEY UPDATE student_id = student_id;
INSERT INTO students (id, first_name, last_name, email, phone, date_of_birth, gender, address, enrollment_date, student_id, department_id, created_at, updated_at)
VALUES (5, 'Eva',     'Martinez','eva@student.com',     '9876543214', '2003-05-20', 'FEMALE', '654 Cedar Ln', '2023-07-01', 'STU2023002', 1, NOW(), NOW()) ON DUPLICATE KEY UPDATE student_id = student_id;

-- Enrollments
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, status, created_at, updated_at) VALUES (1,1,1,'2022-08-01','ACTIVE',NOW(),NOW()) ON DUPLICATE KEY UPDATE status=status;
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, status, created_at, updated_at) VALUES (2,1,3,'2022-08-01','ACTIVE',NOW(),NOW()) ON DUPLICATE KEY UPDATE status=status;
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, status, created_at, updated_at) VALUES (3,1,5,'2022-08-01','ACTIVE',NOW(),NOW()) ON DUPLICATE KEY UPDATE status=status;
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, status, created_at, updated_at) VALUES (4,2,1,'2022-08-01','ACTIVE',NOW(),NOW()) ON DUPLICATE KEY UPDATE status=status;
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, status, created_at, updated_at) VALUES (5,2,2,'2022-08-01','ACTIVE',NOW(),NOW()) ON DUPLICATE KEY UPDATE status=status;
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, status, created_at, updated_at) VALUES (6,3,5,'2023-08-01','ACTIVE',NOW(),NOW()) ON DUPLICATE KEY UPDATE status=status;
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, status, created_at, updated_at) VALUES (7,3,6,'2023-08-01','ACTIVE',NOW(),NOW()) ON DUPLICATE KEY UPDATE status=status;
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, status, created_at, updated_at) VALUES (8,4,7,'2022-08-01','ACTIVE',NOW(),NOW()) ON DUPLICATE KEY UPDATE status=status;
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, status, created_at, updated_at) VALUES (9,5,1,'2023-08-01','ACTIVE',NOW(),NOW()) ON DUPLICATE KEY UPDATE status=status;
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, status, created_at, updated_at) VALUES (10,5,2,'2023-08-01','ACTIVE',NOW(),NOW()) ON DUPLICATE KEY UPDATE status=status;

-- Grades
INSERT INTO grades (id, enrollment_id, marks_obtained, max_marks, grade_letter, remarks, exam_date, created_at, updated_at) VALUES (1,1,88.5,100,'A', 'Excellent','2022-11-20',NOW(),NOW()) ON DUPLICATE KEY UPDATE grade_letter=grade_letter;
INSERT INTO grades (id, enrollment_id, marks_obtained, max_marks, grade_letter, remarks, exam_date, created_at, updated_at) VALUES (2,2,72.0,100,'B', 'Good',     '2022-11-20',NOW(),NOW()) ON DUPLICATE KEY UPDATE grade_letter=grade_letter;
INSERT INTO grades (id, enrollment_id, marks_obtained, max_marks, grade_letter, remarks, exam_date, created_at, updated_at) VALUES (3,4,95.0,100,'A+','Outstanding','2022-11-20',NOW(),NOW()) ON DUPLICATE KEY UPDATE grade_letter=grade_letter;
INSERT INTO grades (id, enrollment_id, marks_obtained, max_marks, grade_letter, remarks, exam_date, created_at, updated_at) VALUES (4,5,78.5,100,'B', 'Good',     '2022-11-20',NOW(),NOW()) ON DUPLICATE KEY UPDATE grade_letter=grade_letter;
INSERT INTO grades (id, enrollment_id, marks_obtained, max_marks, grade_letter, remarks, exam_date, created_at, updated_at) VALUES (5,6,91.0,100,'A+','Outstanding','2023-11-20',NOW(),NOW()) ON DUPLICATE KEY UPDATE grade_letter=grade_letter;
