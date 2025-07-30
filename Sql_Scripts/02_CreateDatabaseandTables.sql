

CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;

-- Students Table
CREATE TABLE student (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    srn VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    branch VARCHAR(50) NOT NULL
);

-- Faculty Table
CREATE TABLE faculty (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    department VARCHAR(50) NOT NULL
);

-- Courses Table
CREATE TABLE course (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    credits INT NOT NULL CHECK (credits BETWEEN 1 AND 5),
    branch VARCHAR(50) NOT NULL,
    semester INT NOT NULL CHECK (semester BETWEEN 1 AND 8)
);

-- Enrollments Table
CREATE TABLE enrollments (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    student_id INT UNSIGNED NOT NULL,
    course_id INT UNSIGNED NOT NULL,
    semester INT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
);

-- Marks Table
CREATE TABLE marks (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    enrollment_id INT UNSIGNED NOT NULL,
    internal1 INT CHECK (internal1 BETWEEN 0 AND 40),
    internal2 INT CHECK (internal2 BETWEEN 0 AND 40),
    endsem INT CHECK (endsem BETWEEN 0 AND 100),
    total INT,
    grade CHAR(1),
    FOREIGN KEY (enrollment_id) REFERENCES enrollments(id) ON DELETE CASCADE
);

-- GPA Table
CREATE TABLE gpa (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    student_id INT UNSIGNED NOT NULL,
    semester INT NOT NULL,
    sgpa DECIMAL(3,2) CHECK (sgpa BETWEEN 0.00 AND 10.00),
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
    UNIQUE KEY uq_student_semester (student_id, semester)
);






