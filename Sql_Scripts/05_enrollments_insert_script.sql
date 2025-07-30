-- 2022 batch (semesters 1–6)
INSERT INTO enrollments (student_id, course_id, semester)
SELECT s.id, c.id, c.semester
FROM student s
JOIN course c ON s.branch = c.branch
WHERE s.srn LIKE 'PES2UG2022%' AND c.semester <= 6;

-- 2023 batch (semesters 1–4)
INSERT INTO enrollments (student_id, course_id, semester)
SELECT s.id, c.id, c.semester
FROM student s
JOIN course c ON s.branch = c.branch
WHERE s.srn LIKE 'PES2UG2023%' AND c.semester <= 4;

-- 2024 batch (semesters 1–2)
INSERT INTO enrollments (student_id, course_id, semester)
SELECT s.id, c.id, c.semester
FROM student s
JOIN course c ON s.branch = c.branch
WHERE s.srn LIKE 'PES2UG2024%' AND c.semester <= 2;
