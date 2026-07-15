CREATE OR REPLACE VIEW v_student_yearly_avg_scores AS
SELECT
    d.txx_DeptNo      AS dept_no,
    d.txx_DeptName    AS dept_name,
    s.txx_Sno         AS sno,
    s.txx_Sname       AS sname,
    SUBSTRING(t.txx_Term FROM 1 FOR 9) AS year,  -- 提取 2023-2024
    ROUND(AVG(r.txx_Grade), 2) AS avg_grade
FROM Tianxx_Reports r
JOIN Tianxx_Students s ON r.txx_Sno = s.txx_Sno
JOIN Tianxx_Teaching t ON r.txx_TeachingID = t.txx_TeachingID
JOIN Tianxx_Courses c ON t.txx_Cno = c.txx_Cno
JOIN Tianxx_Depts d ON s.txx_DeptNo = d.txx_DeptNo
GROUP BY d.txx_DeptNo, d.txx_DeptName, s.txx_Sno, s.txx_Sname, year
ORDER BY year DESC, avg_grade DESC;


CREATE OR REPLACE VIEW vw_teacher_course_student_grades AS
SELECT
    t.txx_Tno AS teacher_no,
    t.txx_Tname AS teacher_name,
    te.txx_TeachingID AS teaching_id,
    c.txx_Cno AS course_no,
    c.txx_Cname AS course_name,
    c.txx_CreditHour AS course_credits,
    c.txx_AssessType AS assess_type,
    cl.txx_ClassID AS class_id,
    cl.txx_ClassName AS class_name,
    te.txx_Term AS term,
    s.txx_Sno AS student_no,
    s.txx_Sname AS student_name,
    s.txx_Sex AS student_sex,
    s.txx_Phone AS student_phone,
    s.txx_Address AS student_address,
    r.txx_Grade AS grade
FROM Tianxx_Teachers t
JOIN Tianxx_Teaching te ON t.txx_Tno = te.txx_Tno
JOIN Tianxx_Courses c ON te.txx_Cno = c.txx_Cno
JOIN Tianxx_Classes cl ON te.txx_ClassID = cl.txx_ClassID
JOIN Tianxx_Students s ON s.txx_ClassID = cl.txx_ClassID
LEFT JOIN Tianxx_Reports r ON r.txx_Sno = s.txx_Sno AND r.txx_TeachingID = te.txx_TeachingID;



-- 按学生查询所有课程及成绩（含未评分课程）
CREATE OR REPLACE VIEW v_student_course_grades AS
SELECT
    s.txx_Sno AS student_no,
    s.txx_Sname AS student_name,
    c.txx_Cno AS course_no,
    c.txx_Cname AS course_name,
    t.txx_Term AS term,
    r.txx_Grade AS grade
FROM Tianxx_Students s
JOIN Tianxx_Classes cl ON s.txx_ClassID = cl.txx_ClassID
JOIN Tianxx_Teaching t ON cl.txx_ClassID = t.txx_ClassID
JOIN Tianxx_Courses c ON t.txx_Cno = c.txx_Cno
LEFT JOIN Tianxx_Reports r ON r.txx_Sno = s.txx_Sno AND r.txx_TeachingID = t.txx_TeachingID
WHERE s.txx_Sno = 'S000001';

-- 按课程统计每学期的平均成绩
CREATE OR REPLACE VIEW v_course_term_avg_grade AS
SELECT
    c.txx_Cno AS course_no,
    c.txx_Cname AS course_name,
    t.txx_Term AS term,
    ROUND(AVG(r.txx_Grade), 2) AS avg_grade
FROM Tianxx_Courses c
JOIN Tianxx_Teaching t ON c.txx_Cno = t.txx_Cno
JOIN Tianxx_Reports r ON t.txx_TeachingID = r.txx_TeachingID
GROUP BY c.txx_Cno, c.txx_Cname, t.txx_Term
ORDER BY t.txx_Term DESC, avg_grade DESC;

-- 查询教师所授课程及班级学生人数
CREATE OR REPLACE VIEW v_teacher_courses_student_count AS
SELECT
    t.txx_Tno AS teacher_no,
    t.txx_Tname AS teacher_name,
    c.txx_Cno AS course_no,
    c.txx_Cname AS course_name,
    te.txx_Term AS term,
    cl.txx_ClassID AS class_id,
    cl.txx_ClassName AS class_name,
    COUNT(s.txx_Sno) AS student_count
FROM Tianxx_Teachers t
JOIN Tianxx_Teaching te ON t.txx_Tno = te.txx_Tno
JOIN Tianxx_Courses c ON te.txx_Cno = c.txx_Cno
JOIN Tianxx_Classes cl ON te.txx_ClassID = cl.txx_ClassID
LEFT JOIN Tianxx_Students s ON s.txx_ClassID = cl.txx_ClassID
GROUP BY t.txx_Tno, t.txx_Tname, c.txx_Cno, c.txx_Cname, te.txx_Term, cl.txx_ClassID, cl.txx_ClassName;

-- 学生各学期累计学分视图
CREATE OR REPLACE VIEW v_student_term_credits AS
SELECT
    s.txx_Sno AS student_no,
    s.txx_Sname AS student_name,
    SUBSTRING(t.txx_Term FROM 1 FOR 9) AS term_year,
    SUM(c.txx_CreditHour) AS total_credits
FROM Tianxx_Students s
JOIN Tianxx_Classes cl ON s.txx_ClassID = cl.txx_ClassID
JOIN Tianxx_Teaching t ON cl.txx_ClassID = t.txx_ClassID
JOIN Tianxx_Courses c ON t.txx_Cno = c.txx_Cno
JOIN Tianxx_Reports r ON r.txx_Sno = s.txx_Sno AND r.txx_TeachingID = t.txx_TeachingID
WHERE r.txx_Grade >= 60  -- 及格学分统计
GROUP BY s.txx_Sno, s.txx_Sname, term_year
ORDER BY s.txx_Sno, term_year DESC;